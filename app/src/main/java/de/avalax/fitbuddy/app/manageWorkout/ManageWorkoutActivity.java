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
import de.avalax.fitbuddy.app.editExercise.WorkoutAdapter;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Inject;
import java.util.List;

public class ManageWorkoutActivity extends ListActivity implements ActionBar.OnNavigationListener, View.OnClickListener {
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";
    private static final String WORKOUT = "WORKOUT";
    private static final String UNSAVED_CHANGES = "UNSAVED_CHANGES";

    public static final int ADD_EXERCISE_BEFORE = 1;
    public static final int EDIT_EXERCISE = 2;
    public static final int ADD_EXERCISE_AFTER = 3;
    public static final int SAVE_WORKOUT = 1;
    public static final int SWITCH_WORKOUT = 2;
    private static final int ADD_EXERCISE = 4;
    private boolean initializing;
    private boolean unsavedChanges;
    @Inject
    protected WorkoutDAO workoutDAO;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected WorkoutSession workoutSession;
    @Inject
    protected WorkoutFactory workoutFactory;
    @Inject
    protected ManageWorkout manageWorkout;
    private int workoutPosition;
    private View footer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        init(savedInstanceState);
        initActionBar();
        initListView();
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            workoutPosition = savedInstanceState.getInt(WORKOUT_POSITION);
            manageWorkout.setWorkout((Workout) savedInstanceState.getSerializable(WORKOUT));
            unsavedChanges = savedInstanceState.getBoolean(UNSAVED_CHANGES);
        } else {
            workoutPosition = sharedPreferences.getInt(WorkoutSession.LAST_WORKOUT_POSITION, 0);
            manageWorkout.setWorkout(workoutDAO.load(workoutPosition));
            unsavedChanges = false;
        }
        footer = findViewById(R.id.footer_undo);
    }

    private void initListView() {
        setListAdapter(WorkoutAdapter.newInstance(getApplication(), R.layout.row, manageWorkout.getWorkout()));
        registerForContextMenu(getListView());
        if (unsavedChanges) {
            showUnsavedChanges();
        }
    }

    private void initActionBar() {
        initializing = true;
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWorkouts());

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        actionBar.setSelectedNavigationItem(workoutPosition);
    }

    private String[] getWorkouts() {
        List<String> workoutlist = workoutDAO.getList();
        if (workoutlist.size() == workoutPosition) {
            workoutlist.add(manageWorkout.getWorkout().getName());
        }
        return workoutlist.toArray(new String[workoutlist.size()]);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startEditExerciseActivity(position);
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

    private void switchWorkout(int itemPosition) {
        manageWorkout.setWorkout(workoutDAO.load(itemPosition));
        workoutPosition = itemPosition;
        initActionBar();
        initListView();
        showUnsavedChanges();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_workout) {
            save();
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.action_add_workout) {
            showNewWorkoutAltertDialog();
        } else if (item.getItemId() == R.id.action_change_workout_name) {
            editWorkoutName();
        } else if (item.getItemId() == R.id.action_delete_workout) {
            deleteWorkout();
        }
        return true;
    }

    private void showNewWorkoutAltertDialog() {
        final CharSequence[] items = {"Create a new workout", "Scan from QR-Code"};
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a workout");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    createNewWorkout();
                } else if (item == 1) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.initiateScan();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void save() {
        workoutDAO.save(manageWorkout.getWorkout());
        workoutSession.switchWorkout(workoutPosition);
    }

    private void createNewWorkout() {
        manageWorkout.setWorkout(workoutFactory.createNew());
        workoutPosition = workoutDAO.getList().size();
        initActionBar();
        initListView();
        showUnsavedChanges();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == getListView().getId()) {
            buildExerciseContextMenu(menu, (AdapterView.AdapterContextMenuInfo) menuInfo);
        }
    }

    private void buildExerciseContextMenu(ContextMenu menu, AdapterView.AdapterContextMenuInfo menuInfo) {
        menu.setHeaderTitle(manageWorkout.getWorkout().getExercise(menuInfo.position).getName());
        String[] menuItems = getResources().getStringArray(R.array.actions_edit_exercise);
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        exerciseActions(item, info.position);
        return true;
    }

    private void exerciseActions(MenuItem item, int exercisePosition) {
        if (getString(R.string.action_exercise_delete).equals(item.getTitle())) {
            deleteExercise(exercisePosition);
        } else if (getString(R.string.action_exercise_add_before_selected).equals(item.getTitle())) {
            startAddExerciseActivity(exercisePosition, ADD_EXERCISE_BEFORE);
        } else if (getString(R.string.action_exercise_add_behind_selected).equals(item.getTitle())) {
            startAddExerciseActivity(exercisePosition, ADD_EXERCISE_AFTER);
        }
    }

    private void startEditExerciseActivity(int position) {
        startAddExerciseActivity(position, EDIT_EXERCISE);
    }

    private void startAddExerciseActivity(int exercisePosition, int action) {
        Intent intent = EditExerciseActivity.newCreateExerciseIntent(this);
        manageWorkout.setExercisePosition(exercisePosition);
        startActivityForResult(intent, action);
    }

    private void deleteExercise(int exercisePosition) {
        manageWorkout.getWorkout().removeExercise(exercisePosition);
        showUnsavedChanges();
        initListView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            try {
                Workout workoutFromJson = workoutFactory.createFromJson(scanResult.getContents());
                if (workoutFromJson != null) {
                    manageWorkout.setWorkout(workoutFromJson);
                    workoutPosition = workoutDAO.getList().size();
                    initActionBar();
                }
                initListView();
                showUnsavedChanges();
            } catch (WorkoutParseException wpe) {
                Toast toast = Toast.makeText(this, getText(R.string.action_read_qrcode_failed), Toast.LENGTH_LONG);
                Log.d("reading of qrcode failed", wpe.getMessage());
                toast.show();
            }
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
            showUnsavedChanges();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(WORKOUT_POSITION, workoutPosition);
        savedInstanceState.putSerializable(WORKOUT, manageWorkout.getWorkout());
        savedInstanceState.putBoolean(UNSAVED_CHANGES, unsavedChanges);
    }

    @OnClick(R.id.button_undo)
    protected void undoChanges() {
        workoutPosition = sharedPreferences.getInt(WorkoutSession.LAST_WORKOUT_POSITION, 0);
        manageWorkout.setWorkout(workoutDAO.load(workoutPosition));
        hideUnsavedChanges();
        initActionBar();
        initListView();
    }

    private void hideUnsavedChanges() {
        unsavedChanges = false;
        footer.setVisibility(View.GONE);
    }

    private void showUnsavedChanges() {
        unsavedChanges = true;
        footer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        startAddExerciseActivity(0, ADD_EXERCISE);
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
                            showUnsavedChanges();
                        }
                        manageWorkout.getWorkout().setName(input.getText().toString());
                        initActionBar();
                    }
                })
                .show();
    }

    private void deleteWorkout() {
        //TODO: undo function remove workout
        workoutDAO.remove(manageWorkout.getWorkout());
        workoutPosition = workoutDAO.getList().size() - 1;
        if (workoutPosition >= 0) {
            manageWorkout.setWorkout(workoutDAO.load(workoutPosition));
            initActionBar();
            initListView();
        } else {
            createNewWorkout();
            workoutDAO.save(manageWorkout.getWorkout());
            hideUnsavedChanges();
        }
    }
}