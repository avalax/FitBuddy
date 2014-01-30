package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import de.avalax.fitbuddy.app.edit.WorkoutAdapter;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class ManageWorkoutActivity extends ListActivity implements ActionBar.OnNavigationListener {
    public static final int SAVE_WORKOUT = 1;
    public static final int SWITCH_WORKOUT = 2;
    @Inject
    protected WorkoutSession workoutSession;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FitbuddyApplication) getApplication()).inject(this);
        initActionBar();
        initListView();
    }

    private void initListView() {
        Workout workout = workoutSession.getWorkout();
        setListAdapter(WorkoutAdapter.newInstance(getApplication(), R.layout.row, workout));
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getWorkouts());

        actionBar.setListNavigationCallbacks(spinnerAdapter, this);
        //TODO: select current workout
        actionBar.setSelectedNavigationItem(0);
    }

    private String[] getWorkouts() {
        return workoutSession.getWorkoutlist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_workout_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        workoutSession.switchWorkout(itemPosition);
        initListView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}