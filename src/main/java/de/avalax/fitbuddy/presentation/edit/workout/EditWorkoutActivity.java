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
import butterknife.ButterKnife;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.*;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class EditWorkoutActivity extends FragmentActivity implements ActionBar.OnNavigationListener, EditNameDialogFragment.DialogListener {

    public static final int EDIT_EXERCISE = 2;
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";
    private boolean initializing;
    @Inject
    protected EditWorkoutApplicationService editWorkoutApplicationService;
    @Inject
    protected WorkoutSession workoutSession;
    @Inject
    protected WorkoutService workoutService;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    private ExerciseListFragment exerciseListFragment;
    private List<WorkoutListEntry> workoutList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ButterKnife.inject(this);
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
            editWorkoutApplicationService.setWorkout(workoutId);
        } catch (RessourceNotFoundException e) {
            Log.d("create a new workout", e.getMessage(), e);
            editWorkoutApplicationService.createWorkout();
        }
        exerciseListFragment = new ExerciseListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, exerciseListFragment).commit();
        initActionBar();
        initActionNavigationBar();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(WORKOUT_POSITION, editWorkoutApplicationService.getWorkout().getWorkoutId().id());
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
            switchWorkout(workoutId);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_workout) {
            try {
                editWorkoutApplicationService.switchWorkout();
            } catch (IOException e) {
                Log.d("Can't switch workout", e.getMessage(), e);
            }
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_workout) {
            showNewWorkoutAltertDialog();
        } else if (item.getItemId() == R.id.action_change_workout_name) {
            if (editWorkoutApplicationService.getWorkout() == null) {
                editWorkoutApplicationService.createWorkout();
                initActionNavigationBar();
            }
            editWorkoutName();
        } else if (item.getItemId() == R.id.action_delete_workout) {
            editWorkoutApplicationService.deleteWorkout();
            List<WorkoutListEntry> workouts = editWorkoutApplicationService.getWorkoutList();
            if (workouts.size() > 0) {
                WorkoutId workoutId = workouts.get(0).getWorkoutId();
                try {
                    editWorkoutApplicationService.setWorkout(workoutId);
                } catch (WorkoutNotFoundException wnfw) {
                    Log.d("MangeWorkoutActivity", wnfw.getMessage(), wnfw);
                }
            }
            initActionNavigationBar();
            exerciseListFragment.initListView();
        } else if (item.getItemId() == R.id.action_add_exercise) {
            if (editWorkoutApplicationService.getWorkout() == null) {
                editWorkoutApplicationService.createWorkout();
                initActionNavigationBar();
            }
            editWorkoutApplicationService.createExercise();
            exerciseListFragment.initListView();
        } else if (item.getItemId() == R.id.action_share_workout) {
            displayQrCode();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EDIT_EXERCISE && resultCode == RESULT_OK) {
            exerciseListFragment.initListView();
        }
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            createWorkoutFromJson(scanResult.getContents());
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
        workoutList = editWorkoutApplicationService.getWorkoutList();
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, workoutList);

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        selectNavigationItem();
    }

    private void selectNavigationItem() {
        ActionBar actionBar = getActionBar();
        for (int i = 0; i < workoutList.size(); i++) {
            if (workoutList.get(i).getWorkoutId().equals(editWorkoutApplicationService.getWorkout().getWorkoutId())) {
                actionBar.setSelectedNavigationItem(i);
            }
        }
    }

    private void switchWorkout(WorkoutId workoutId) {
        try {
            editWorkoutApplicationService.setWorkout(workoutId);
            exerciseListFragment.initListView();
        } catch (WorkoutNotFoundException wnfe) {
            Log.d("ManageWorkoutActivity", wnfe.getMessage(), wnfe);
        }
    }

    private void showNewWorkoutAltertDialog() {
        //TODO: split into two actions and remove dialog
        final CharSequence[] items = {"Create a new workout", "Scan from QR-Code"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a workout");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    editWorkoutApplicationService.createWorkout();
                    initActionNavigationBar();
                    exerciseListFragment.initListView();
                } else if (item == 1) {
                    IntentIntegrator integrator = new IntentIntegrator(EditWorkoutActivity.this);
                    integrator.initiateScan();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createWorkoutFromJson(String jsonString) {
        try {
            editWorkoutApplicationService.createWorkoutFromJson(jsonString);
            initActionNavigationBar();
            exerciseListFragment.initListView();
        } catch (WorkoutParseException wpe) {
            Toast toast = Toast.makeText(this, getText(R.string.action_read_qrcode_failed), Toast.LENGTH_LONG);
            Log.d("reading of qrcode failed", wpe.getMessage(), wpe);
            toast.show();
        }
    }

    private void displayQrCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("ENCODE_SHOW_CONTENTS", false);
        integrator.shareText(workoutService.jsonFromWorkout(editWorkoutApplicationService.getWorkout()));
    }

    private void editWorkoutName() {
        FragmentManager fm = getSupportFragmentManager();
        String name = editWorkoutApplicationService.getWorkout().getName();
        String hint = getResources().getString(R.string.new_workout_name);
        EditNameDialogFragment.newInstance(name, hint).show(fm, "fragment_edit_name");
    }

    @Override
    public void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment) {
        String name = editNameDialogFragment.getName();
        if (!editWorkoutApplicationService.getWorkout().getName().equals(name)) {
            editWorkoutApplicationService.changeName(name);
            initActionNavigationBar();
        }
    }
}