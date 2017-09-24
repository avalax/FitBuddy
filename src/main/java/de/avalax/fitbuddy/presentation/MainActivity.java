package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_WORKOUT = 1;
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
        intent.putExtra("workout", new BasicWorkout());
        startActivityForResult(intent, ADD_WORKOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_WORKOUT && resultCode == Activity.RESULT_OK) {
            Workout workout = (Workout) data.getSerializableExtra("workout");
            addWorkoutToList(workout);
        }
        if (requestCode == EDIT_WORKOUT && resultCode == Activity.RESULT_OK) {
            Workout workout = (Workout) data.getSerializableExtra("workout");
            Integer position = data.getIntExtra("position", -1);
            updateWorkout(position, workout);
        }
    }

    private void updateWorkout(Integer position, Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
        workoutListFragment.updateWorkout(position, workout);
    }

    private void addWorkoutToList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
        workoutListFragment.addWorkout(workout);
    }
}
