package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParserService;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;

import static com.google.zxing.integration.android.IntentIntegrator.parseActivityResult;
import static de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment.DialogListener;
import static de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment.newInstance;

public class EditWorkoutActivity extends FragmentActivity
        implements ActionBar.OnNavigationListener, DialogListener {

    public static final int EDIT_EXERCISE = 2;
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";
    private boolean initializing;
    @Inject
    protected EditWorkoutApplicationService editEditWorkoutApplicationService;
    @Inject
    protected WorkoutParserService workoutParserService;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    private ExerciseListFragment exerciseListFragment;
    private List<WorkoutListEntry> workoutList;
    private Workout workout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ((FitbuddyApplication) getApplication()).inject(this);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        WorkoutId workoutId = null;
        if (savedInstanceState != null) {
            workoutId = new WorkoutId(savedInstanceState.getString(WORKOUT_POSITION));
        }

        try {
            if (workoutId == null) {
                workoutId = workoutApplicationService.currentWorkoutId();
            }
            workout = editEditWorkoutApplicationService.loadWorkout(workoutId);
        } catch (ResourceException e) {
            Log.d("create a new workout", e.getMessage(), e);
            workout = editEditWorkoutApplicationService.createWorkout();
        }
        exerciseListFragment = new ExerciseListFragment();
        Bundle args = new Bundle();
        args.putSerializable("workoutId", workoutId);
        exerciseListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, exerciseListFragment).commit();
        initActionBar();
        initActionNavigationBar();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String workoutId = workout.getWorkoutId().getId();
        savedInstanceState.putString(WORKOUT_POSITION, workoutId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_workout_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (initializing) {
            initializing = false;
        } else {
            WorkoutId workoutId = workoutList.get(itemPosition).getWorkoutId();
            try {
                switchWorkout(workoutId);
            } catch (WorkoutException e) {
                Log.d("Can't load workout", e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (R.id.action_save_workout == itemId) {
            saveWorkout();
        } else if (R.id.action_add_workout == itemId) {
            showNewWorkoutAlertDialog();
        } else if (R.id.action_change_workout_name == itemId) {
            editWorkoutName();
        } else if (R.id.action_delete_workout == itemId) {
            try {
                deleteWorkout();
            } catch (WorkoutException e) {
                Log.d("Can't load new workout", e.getMessage(), e);
            }
        } else if (R.id.action_add_exercise == itemId) {
            try {
                addExercise();
            } catch (WorkoutException e) {
                Log.d("Can't add exercise", e.getMessage(), e);
            }
        } else if (R.id.action_share_workout == itemId) {
            displayQrCode();
        }
        return true;
    }

    private void addExercise() throws WorkoutException {
        editEditWorkoutApplicationService.createExercise(workout);
        exerciseListFragment.initListView(workout.getWorkoutId());
    }

    private void deleteWorkout() throws WorkoutException {
        editEditWorkoutApplicationService.deleteWorkout(workout);
        List<WorkoutListEntry> workouts = editEditWorkoutApplicationService.getWorkoutList();
        if (!workouts.isEmpty()) {
            WorkoutId workoutId = workouts.get(0).getWorkoutId();
            try {
                workout = editEditWorkoutApplicationService.loadWorkout(workoutId);
            } catch (WorkoutException e) {
                workout = editEditWorkoutApplicationService.createWorkout();
            }
        }
        initActionNavigationBar();
        exerciseListFragment.initListView(workout.getWorkoutId());
    }

    private void saveWorkout() {
        try {
            editEditWorkoutApplicationService.switchWorkout(workout);
        } catch (WorkoutException e) {
            Log.d("Can't switch workout", e.getMessage(), e);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EDIT_EXERCISE && resultCode == RESULT_OK) {
            try {
                exerciseListFragment.initListView(workout.getWorkoutId());
                workout = editEditWorkoutApplicationService.loadWorkout(workout.getWorkoutId());
            } catch (WorkoutException e) {
                Log.d("Failed to init fragment", e.getMessage(), e);
            }
        }
        IntentResult scanResult = parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            try {
                createWorkoutFromJson(scanResult.getContents());
            } catch (WorkoutException e) {
                Log.d("Create from JSON failed", e.getMessage(), e);
            }
        }
    }

    private void initActionBar() {
        initializing = true;
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    protected void initActionNavigationBar() {
        ActionBar actionBar = getActionBar();
        initializing = true;
        workoutList = editEditWorkoutApplicationService.getWorkoutList();
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, workoutList);

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        selectNavigationItem();
    }

    private void selectNavigationItem() {
        ActionBar actionBar = getActionBar();
        WorkoutId selectedWorkoutId = workout.getWorkoutId();
        for (int i = 0; i < workoutList.size(); i++) {
            if (workoutList.get(i).getWorkoutId().equals(selectedWorkoutId)) {
                actionBar.setSelectedNavigationItem(i);
            }
        }
    }

    private void switchWorkout(WorkoutId workoutId) throws WorkoutException {
        workout = editEditWorkoutApplicationService.loadWorkout(workoutId);
        exerciseListFragment.initListView(workoutId);
    }

    private void showNewWorkoutAlertDialog() {
        final CharSequence[] items = {"Create a new workout", "Scan from QR-Code"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a workout");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    editEditWorkoutApplicationService.createWorkout();
                    initActionNavigationBar();
                    try {
                        exerciseListFragment.initListView(workout.getWorkoutId());
                    } catch (WorkoutException e) {
                        Log.d("Failed to init fragment", e.getMessage(), e);
                    }
                } else if (item == 1) {
                    IntentIntegrator integrator = new IntentIntegrator(EditWorkoutActivity.this);
                    integrator.initiateScan();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createWorkoutFromJson(String jsonString) throws WorkoutException {
        try {
            editEditWorkoutApplicationService.createWorkoutFromJson(jsonString);
            initActionNavigationBar();
            exerciseListFragment.initListView(workout.getWorkoutId());
        } catch (WorkoutParseException wpe) {
            CharSequence text = getText(R.string.action_read_qrcode_failed);
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            Log.d("failed reading qrcode", wpe.getMessage(), wpe);
            toast.show();
        }
    }

    private void displayQrCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("ENCODE_SHOW_CONTENTS", false);
        integrator.shareText(workoutParserService.jsonFromWorkout(workout));
    }

    private void editWorkoutName() {
        FragmentManager fm = getSupportFragmentManager();
        String name = workout.getName();
        String hint = getResources().getString(R.string.new_workout_name);
        newInstance(name, hint).show(fm, "fragment_edit_name");
    }

    @Override
    public void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment) {
        String name = editNameDialogFragment.getName();
        if (!workout.getName().equals(name)) {
            editEditWorkoutApplicationService.changeName(workout, name);
            initActionNavigationBar();
        }
    }
}