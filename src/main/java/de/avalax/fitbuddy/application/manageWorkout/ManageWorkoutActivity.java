package de.avalax.fitbuddy.application.manageWorkout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import de.avalax.fitbuddy.application.FitbuddyApplication;
import de.avalax.fitbuddy.application.R;
import de.avalax.fitbuddy.application.WorkoutParseException;
import de.avalax.fitbuddy.application.WorkoutSession;
import de.avalax.fitbuddy.application.manageWorkout.events.WorkoutListInvalidatedEvent;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;

import javax.inject.Inject;
import java.util.List;

public class ManageWorkoutActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
    public static final int EDIT_EXERCISE = 2;
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";
    private boolean initializing;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected ManageWorkout manageWorkout;
    @Inject
    protected Bus bus;
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
        String lastWorkouId;
        if (savedInstanceState != null) {
            lastWorkouId = savedInstanceState.getString(WORKOUT_POSITION);
        } else {
            lastWorkouId = sharedPreferences.getString(WorkoutSession.LAST_WORKOUT_POSITION, "1");
        }
        manageWorkout.setWorkout(new WorkoutId(lastWorkouId));
        exerciseListFragment = new ExerciseListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, exerciseListFragment).commit();
        initActionBar();
        initActionNavigationBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(WORKOUT_POSITION, manageWorkout.getWorkout().getWorkoutId().id());
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
            WorkoutId workoutId = workoutList.get(itemPosition).getWorkoutId();
            switchWorkout(workoutId);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_workout) {
            manageWorkout.switchWorkout();
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_workout) {
            showNewWorkoutAltertDialog();
        } else if (item.getItemId() == R.id.action_change_workout_name) {
            editWorkoutName();
        } else if (item.getItemId() == R.id.action_delete_workout) {
            manageWorkout.deleteWorkout();
            List<WorkoutListEntry> workouts = manageWorkout.getWorkoutList();
            if (workouts.size() == 0) {
                manageWorkout.createWorkout();
            }
            //TODO: refactor this to mangageWorkout.loadFirstWorkout()
            manageWorkout.setWorkout(manageWorkout.getWorkoutList().get(0).getWorkoutId());
            initActionNavigationBar();
            exerciseListFragment.initListView();
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

    private void initActionNavigationBar() {
        ActionBar actionBar = getActionBar();
        initializing = true;
        workoutList = manageWorkout.getWorkoutList();
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, workoutList);

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        selectNavigationItem();
    }

    private void selectNavigationItem() {
        ActionBar actionBar = getActionBar();
        for (int i = 0; i < workoutList.size(); i++) {
            if (workoutList.get(i).getWorkoutId().equals(manageWorkout.getWorkout().getWorkoutId())) {
                actionBar.setSelectedNavigationItem(i);
            }
        }
    }

    private void switchWorkout(WorkoutId workoutId) {
        manageWorkout.setWorkout(workoutId);
        exerciseListFragment.initListView();
    }

    private void showNewWorkoutAltertDialog() {
        //TODO: split into two actions and remove dialog
        final CharSequence[] items = {"Create a new workout", "Scan from QR-Code"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a workout");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    manageWorkout.createWorkout();
                    initActionNavigationBar();
                    exerciseListFragment.initListView();
                } else if (item == 1) {
                    IntentIntegrator integrator = new IntentIntegrator(ManageWorkoutActivity.this);
                    integrator.initiateScan();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createWorkoutFromJson(String jsonString) {
        try {
            manageWorkout.createWorkoutFromJson(jsonString);
            initActionNavigationBar();
            exerciseListFragment.initListView();
        } catch (WorkoutParseException wpe) {
            Toast toast = Toast.makeText(this, getText(R.string.action_read_qrcode_failed), Toast.LENGTH_LONG);
            Log.d("reading of qrcode failed", wpe.getMessage(), wpe);
            toast.show();
        }
    }

    private void editWorkoutName() {
        //TODO: make a DialogFragment
        final EditText input = new EditText(this);
        input.setText(manageWorkout.getWorkout().getName());
        new AlertDialog.Builder(this)
                .setTitle("workout name")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!manageWorkout.getWorkout().getName().equals(input.getText().toString())) {
                            manageWorkout.changeName(input.getText().toString());
                            initActionNavigationBar();
                        }
                    }
                })
                .show();
    }

    @Subscribe
    public void onWorkoutListInvalidated(WorkoutListInvalidatedEvent event) {
        initActionNavigationBar();
        exerciseListFragment.initListView();
    }
}