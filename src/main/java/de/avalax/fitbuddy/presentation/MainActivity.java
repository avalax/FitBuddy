package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_WORKOUT = 1;
    public static final int EDIT_WORKOUT = 2;
    @Inject
    protected WorkoutRepository workoutRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddWorkoutButtonClick(View view) {
        Intent intent = new Intent(this, EditWorkoutActivity.class);
        startActivityForResult(intent, NEW_WORKOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORKOUT && resultCode == Activity.RESULT_OK) {
            WorkoutId workoutId = (WorkoutId) data.getSerializableExtra("workout_id");
            addWorkoutToList(workoutId);
        }
        if (requestCode == EDIT_WORKOUT && resultCode == Activity.RESULT_OK) {
            WorkoutId workoutId = (WorkoutId) data.getSerializableExtra("workout_id");
            Integer position = data.getIntExtra("position", -1);
            updateWorkout(position, workoutId);
        }
    }

    private void updateWorkout(Integer position, WorkoutId workoutId) {
        try {
            Workout workout = workoutRepository.load(workoutId);
            WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            workoutListFragment.updateWorkout(position, workout);
        } catch (WorkoutException e) {
            Log.e("DATA_EXCEPTION", e.getMessage());
        }
    }

    private void addWorkoutToList(WorkoutId workoutId) {
        try {
            Workout workout = workoutRepository.load(workoutId);
            WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            workoutListFragment.addWorkout(workout);
        } catch (WorkoutException e) {
            Log.e("DATA_EXCEPTION", e.getMessage());
        }
    }
}
