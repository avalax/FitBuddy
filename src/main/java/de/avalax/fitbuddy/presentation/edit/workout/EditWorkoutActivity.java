package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class EditWorkoutActivity extends AppCompatActivity {

    private static final int EDIT_EXERCISE = 2;
    @Inject
    protected WorkoutRepository workoutRepository;
    private EditText nameEditText;
    private Workout workout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        ((FitbuddyApplication) getApplication()).getComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_workout);
        setSupportActionBar(toolbar);
        nameEditText = findViewById(R.id.edit_text_workout_name);
        workout = new BasicWorkout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_workout, menu);

        return true;
    }

    public void onAddExerciseButtonClick(View view) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        startActivityForResult(intent, EDIT_EXERCISE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EXERCISE && resultCode == Activity.RESULT_OK) {
            String name = data.getStringExtra("name");
            Sets sets = (Sets) data.getSerializableExtra("sets");
            Exercise exercise = workout.getExercises().createExercise();
            exercise.setName(name);
            for (Set s : sets) {
                Set set = exercise.getSets().createSet();
                set.setReps(s.getMaxReps());
                set.setWeight(s.getWeight());
            }

            ExerciseListFragment workoutListFragment = (ExerciseListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            workoutListFragment.addExercise(exercise);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_workout) {
            workout.setName(nameEditText.getText().toString());
            workoutRepository.save(workout);
            Intent intent = new Intent();
            intent.putExtra("workout_id", workout.getWorkoutId());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }
}