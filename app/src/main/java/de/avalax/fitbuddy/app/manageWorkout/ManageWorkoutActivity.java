package de.avalax.fitbuddy.app.manageWorkout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import de.avalax.fitbuddy.app.*;
import de.avalax.fitbuddy.app.editExercise.EditExerciseActivity;
import de.avalax.fitbuddy.app.editExercise.EditableExercise;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;
import java.util.List;

public class ManageWorkoutActivity extends ListActivity implements ActionBar.OnNavigationListener, View.OnClickListener {
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";

    public static final int ADD_EXERCISE_BEFORE = 1;
    public static final int EDIT_EXERCISE = 2;
    public static final int ADD_EXERCISE_AFTER = 3;
    public static final int SAVE_WORKOUT = 1;
    public static final int SWITCH_WORKOUT = 2;
    private static final int ADD_EXERCISE = 4;
    private boolean initializing;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected ManageWorkout manageWorkout;
    private int workoutPosition;
    private View footer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        init(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(WORKOUT_POSITION, workoutPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_workout_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (initializing) {
            initializing = false;
        } else {
            switchWorkout(itemPosition);
        }
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        editExercise(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_workout) {
            manageWorkout.switchWorkout(workoutPosition);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_workout) {
            showNewWorkoutAltertDialog();
        } else if (item.getItemId() == R.id.action_change_workout_name) {
            editWorkoutName();
        } else if (item.getItemId() == R.id.action_delete_workout) {
            manageWorkout.deleteWorkout();
            initActionBar();
            initListView();
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (getString(R.string.action_exercise_delete).equals(item.getTitle())) {
            deleteExercise(info.position);
        } else if (getString(R.string.action_exercise_add_before_selected).equals(item.getTitle())) {
            startAddExerciseActivity(info.position, ADD_EXERCISE_BEFORE);
        } else if (getString(R.string.action_exercise_add_behind_selected).equals(item.getTitle())) {
            startAddExerciseActivity(info.position, ADD_EXERCISE_AFTER);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            createWorkoutFromJson(scanResult.getContents());
        } else if (resultCode == Activity.RESULT_OK) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra("editableExercise");
            if (requestCode == ADD_EXERCISE) {
                manageWorkout.getWorkout().addExercise(editableExercise.createExercise());
            } else if (requestCode == ADD_EXERCISE_BEFORE) {
                manageWorkout.getWorkout().addExerciseBefore(manageWorkout.getExercisePosition(), editableExercise.createExercise());
            } else if (requestCode == ADD_EXERCISE_AFTER) {
                manageWorkout.getWorkout().addExerciseAfter(manageWorkout.getExercisePosition(), editableExercise.createExercise());
            } else if (requestCode == EDIT_EXERCISE) {
                manageWorkout.getWorkout().setExercise(manageWorkout.getExercisePosition(), editableExercise.createExercise());
            }
            initListView();
        }
    }

    @Override
    public void onClick(View v) {
        startAddExerciseActivity(0, ADD_EXERCISE);
    }

    @OnClick(R.id.button_undo)
    protected void undoChanges() {
        manageWorkout.undoUnsavedChanges();
        initActionBar();
        initListView();
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            workoutPosition = savedInstanceState.getInt(WORKOUT_POSITION);
        } else {
            workoutPosition = sharedPreferences.getInt(WorkoutSession.LAST_WORKOUT_POSITION, 0);
        }
        manageWorkout.setWorkout(workoutPosition);
        footer = findViewById(R.id.footer_undo);
        initActionBar();
        initListView();
    }

    private void initListView() {
        setListAdapter(WorkoutAdapter.newInstance(getApplication(), R.layout.row, manageWorkout.getWorkout()));
        registerForContextMenu(getListView());
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
    }

    private void initActionBar() {
        initializing = true;
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWorkouts());

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        actionBar.setSelectedNavigationItem(workoutPosition);
    }

    private String[] getWorkouts() {
        List<String> workoutlist = manageWorkout.getWorkouts();
        if (workoutlist.size() == workoutPosition) {
            workoutlist.add(manageWorkout.getWorkout().getName());
        }
        return workoutlist.toArray(new String[workoutlist.size()]);
    }

    private void switchWorkout(int position) {
        manageWorkout.setWorkout(position);
        workoutPosition = position;
        initActionBar();
        initListView();
    }

    private void showNewWorkoutAltertDialog() {
        final CharSequence[] items = {"Create a new workout", "Scan from QR-Code"};
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a workout");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    manageWorkout.createNewWorkout();
                    workoutPosition = manageWorkout.getWorkouts().size() - 1;
                    initActionBar();
                    initListView();
                } else if (item == 1) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.initiateScan();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startAddExerciseActivity(int exercisePosition, int action) {
        Intent intent = EditExerciseActivity.newCreateExerciseIntent(this);
        manageWorkout.setExercisePosition(exercisePosition);
        startActivityForResult(intent, action);
    }

    private void deleteExercise(int exercisePosition) {
        manageWorkout.getWorkout().removeExercise(exercisePosition);
        manageWorkout.setUnsavedChanges(true);
        //TODO: undo remove exercise
        initListView();
    }

    private void createWorkoutFromJson(String jsonString) {
        try {
            manageWorkout.createWorkoutFromJson(jsonString);
            workoutPosition = manageWorkout.getWorkouts().size() - 1;
            initActionBar();
            initListView();
        } catch (WorkoutParseException wpe) {
            Toast toast = Toast.makeText(this, getText(R.string.action_read_qrcode_failed), Toast.LENGTH_LONG);
            Log.d("reading of qrcode failed", wpe.getMessage());
            toast.show();
        }
    }

    private void editWorkoutName() {
        final EditText input = new EditText(this);
        input.setText(manageWorkout.getWorkout().getName());
        new AlertDialog.Builder(this)
                .setTitle("workout name")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!manageWorkout.getWorkout().getName().equals(input.getText().toString())) {
                            manageWorkout.getWorkout().setName(input.getText().toString());
                            initActionBar();
                        }
                    }
                })
                .show();
    }

    private void editExercise(int position) {
        Exercise exercise = manageWorkout.getWorkout().getExercise(position);
        final ManageWorkoutActivity manageWorkoutActivity = this;
        View view = getLayoutInflater().inflate(R.layout.view_edit_exercise, null);
        new AlertDialog.Builder(this)
                .setTitle(exercise.getName())
                .setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(manageWorkoutActivity, "delete", 1000);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(manageWorkoutActivity, "save", 1000);
                            }
                        }
                )
                .show();
    }
}