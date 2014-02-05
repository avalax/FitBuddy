package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
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
        registerForContextMenu(getListView());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == getListView().getId()) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(workoutSession.getWorkout().getName(info.position));
            String[] menuItems = getResources().getStringArray(R.array.actions_edit_exercise);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d("onCreateContextMenu", String.valueOf(info.position) + "_" + String.valueOf(item.getItemId()));
        return true;
    }
}