package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_EXERCISE;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_EXERCISE;
import static java.lang.String.valueOf;

public class EditWorkoutActivity extends AppCompatActivity {
    @Inject
    EditWorkoutService editWorkoutService;
    private EditText nameEditText;
    private Workout workout;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar_workout_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        nameEditText = findViewById(R.id.edit_text_workout_name);
        workout = (Workout) getIntent().getSerializableExtra("workout");
        nameEditText.setText(workout.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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
                    getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            exerciseListFragment.notifyItemInserted();
        }
        if (requestCode == EDIT_EXERCISE && resultCode == Activity.RESULT_OK) {
            Integer position = data.getIntExtra("position", -1);
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            workout.getExercises().set(position, exercise);
            ExerciseListFragment exerciseListFragment = (ExerciseListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            exerciseListFragment.notifyItemChanged(position);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_workout) {
            if (workout.getExercises().size() == 0) {
                Context context = getApplicationContext();
                makeText(context, R.string.message_save_workout_without_exercices, LENGTH_SHORT)
                        .show();
            } else {
                workout.setName(nameEditText.getText().toString());
                try {
                    editWorkoutService.saveWorkout(workout);
                } catch (ResourceException e) {
                    Log.e("ResourceException", e.getMessage(), e);
                }
                Intent intent = new Intent();
                intent.putExtra("workout", workout);
                int position = getIntent().getIntExtra("position", -1);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        }
        if (item.getItemId() == R.id.toolbar_delete_exercices) {
            ExerciseListFragment setListFragment = (ExerciseListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            setListFragment.removeSelections();
            return true;
        }
        return false;
    }

    public void updateToolbar(int selectionCount) {
        if (selectionCount > 0) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_edit_workout_delete_exercices, menu);
            MenuItem item = menu.findItem(R.id.toolbar_delete_exercices);
            item.setTitle(valueOf(selectionCount));
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_edit_workout, menu);
        }
    }

    public void onCancelButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}