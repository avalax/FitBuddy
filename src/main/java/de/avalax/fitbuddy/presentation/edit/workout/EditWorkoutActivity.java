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
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

public class EditWorkoutActivity extends AppCompatActivity {

    public static final int ADD_EXERCISE = 3;
    public static final int EDIT_EXERCISE = 4;
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
        workout = (Workout) getIntent().getSerializableExtra("workout");
        nameEditText.setText(workout.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_workout, menu);

        return true;
    }

    public void onAddExerciseButtonClick(View view) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        intent.putExtra("exercise", new BasicExercise());
        startActivityForResult(intent, ADD_EXERCISE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISE && resultCode == Activity.RESULT_OK) {
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            workout.getExercises().add(exercise);
            ExerciseListFragment exerciseListFragment = (ExerciseListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            exerciseListFragment.addExercise();
        }
        if (requestCode == EDIT_EXERCISE && resultCode == Activity.RESULT_OK) {
            Integer position = data.getIntExtra("position", -1);
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            workout.getExercises().set(position, exercise);
            ExerciseListFragment exerciseListFragment1 = (ExerciseListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
            exerciseListFragment1.updateExercise(position, exercise);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_workout) {
            workout.setName(nameEditText.getText().toString());
            workoutRepository.save(workout);
            Intent intent = new Intent();
            intent.putExtra("workout", workout);
            int position = getIntent().getIntExtra("position", -1);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }
}