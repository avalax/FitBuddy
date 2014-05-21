package de.avalax.fitbuddy.app.manageWorkout;

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
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.WorkoutParseException;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseChangedEvent;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseDeletedEvent;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseListInvalidatedEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.TreeMap;

public class ManageWorkoutActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
    private static final String WORKOUT_POSITION = "WORKOUT_POSITION";
    private boolean initializing;
    @Inject
    protected SharedPreferences sharedPreferences;
    @Inject
    protected ManageWorkout manageWorkout;
    @Inject
    protected Bus bus;
    private long workoutPosition;
    private ExerciseListFragment exerciseListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            workoutPosition = savedInstanceState.getLong(WORKOUT_POSITION);
        } else {
            workoutPosition = sharedPreferences.getLong(WorkoutSession.LAST_WORKOUT_POSITION, 1L);
        }
        manageWorkout.setWorkout(workoutPosition);
        exerciseListFragment = new ExerciseListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, exerciseListFragment).commit();
        initActionBar();
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
        savedInstanceState.putLong(WORKOUT_POSITION, workoutPosition);
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
            initActionBar();
            exerciseListFragment.initListView();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
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

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWorkouts());

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        //TODO: select navigationItem
        //actionBar.setSelectedNavigationItem(workoutPosition);
    }

    private String[] getWorkouts() {
        //FIXME: make it work with a map
        TreeMap<Long, String> workoutlist = manageWorkout.getWorkouts();
        if (workoutlist.size() == workoutPosition) {
            workoutlist.put(null,manageWorkout.getWorkout().getName());
        }
        return workoutlist.values().toArray(new String[workoutlist.size()]);
    }

    private void switchWorkout(int position) {
        manageWorkout.setWorkout(position);
        workoutPosition = position;
        initActionBar();
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
                    manageWorkout.createNewWorkout();
                    workoutPosition = manageWorkout.getWorkouts().size() - 1;
                    initActionBar();
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
            workoutPosition = manageWorkout.getWorkouts().size() - 1;
            initActionBar();
            exerciseListFragment.initListView();
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

    @Subscribe
    public void onExerciseChanged(ExerciseChangedEvent event) {
        manageWorkout.setExercise(event.position, event.exercise);
        bus.post(new ExerciseListInvalidatedEvent());
    }

    @Subscribe
    public void onExerciseDeleted(ExerciseDeletedEvent event) {
        manageWorkout.deleteExercise(event.position);
        bus.post(new ExerciseListInvalidatedEvent());
    }
}