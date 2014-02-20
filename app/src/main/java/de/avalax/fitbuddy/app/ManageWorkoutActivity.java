package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import de.avalax.fitbuddy.app.edit.EditExerciseActivity;
import de.avalax.fitbuddy.app.edit.WorkoutAdapter;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class ManageWorkoutActivity extends ListActivity implements ActionBar.OnNavigationListener {
    public static final int ADD_EXERCISE_BEFORE = 1;
    public static final int EDIT_EXERCISE = 2;
    public static final int ADD_EXERCISE_AFTER = 3;

    public static final int SAVE_WORKOUT = 1;
    public static final int SWITCH_WORKOUT = 2;
    @Inject
    protected WorkoutSession workoutSession;
    @Inject
    protected SharedPreferences sharedPreferences;

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
        int workoutPosition = sharedPreferences.getInt(WorkoutSession.LAST_WORKOUT_POSITION, 0);
        actionBar.setSelectedNavigationItem(workoutPosition);
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
        if (item.getItemId() == R.id.action_select_exercise) {
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.action_add_workout) {
            Log.d("onOptionsItemSelected","action_add_workout");
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
        int exercisePosition = info.position;
        if (getString(R.string.action_exercise_edit).equals(item.getTitle())) {
            Intent intent = EditExerciseActivity.newEditExerciseIntent(this, exercisePosition);
            startActivityForResult(intent, EDIT_EXERCISE);
        } else if (getString(R.string.action_exercise_delete).equals(item.getTitle())) {
            workoutSession.getWorkout().removeExercise(exercisePosition);
            initListView();
        } else if (getString(R.string.action_exercise_add_before_selected).equals(item.getTitle())) {
            Intent intent = EditExerciseActivity.newCreateExerciseIntent(this, exercisePosition, ADD_EXERCISE_BEFORE);
            startActivityForResult(intent, ADD_EXERCISE_BEFORE);
        } else if (getString(R.string.action_exercise_add_behind_selected).equals(item.getTitle())) {
            Intent intent = EditExerciseActivity.newCreateExerciseIntent(this, exercisePosition, ADD_EXERCISE_AFTER);
            startActivityForResult(intent, ADD_EXERCISE_AFTER);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            initListView();
        }
    }
}