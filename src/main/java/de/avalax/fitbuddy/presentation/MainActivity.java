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

    private static final int EDIT_WORKOUT = 1;
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
        startActivityForResult(intent, EDIT_WORKOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_WORKOUT && resultCode == Activity.RESULT_OK) {
            WorkoutId workout_id = (WorkoutId) data.getSerializableExtra("workout_id");
            addWorkoutToList(workout_id);
        }
    }

    private void addWorkoutToList(WorkoutId workout_id) {
        try {
            Workout workout = workoutRepository.load(workout_id);
            WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            workoutListFragment.addWorkout(workout);
        } catch (WorkoutException e) {
            Log.e("DATA_EXCEPTION", e.getMessage());
        }
    }
}
