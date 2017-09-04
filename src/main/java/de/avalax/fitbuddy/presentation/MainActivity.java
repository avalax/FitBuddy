package de.avalax.fitbuddy.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;

public class MainActivity extends AppCompatActivity {

    @Inject
    protected WorkoutRepository workoutRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddWorkoutButtonClick(View view) {
        Workout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        Set set = exercise.getSets().createSet();
        set.setReps(12);
        workoutRepository.save(workout);

        WorkoutListFragment workoutListFragment = (WorkoutListFragment) getSupportFragmentManager()
                .findFragmentById((R.id.toolbar_fragment));
        workoutListFragment.addWorkout(workout);
    }
}
