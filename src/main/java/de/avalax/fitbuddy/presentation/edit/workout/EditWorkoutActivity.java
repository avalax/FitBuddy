package de.avalax.fitbuddy.presentation.edit.workout;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

import static android.support.v4.app.FragmentTransaction.TRANSIT_NONE;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_EXERCISE;
import static java.lang.String.valueOf;

public class EditWorkoutActivity extends AppCompatActivity {
    @Inject
    EditWorkoutService editWorkoutService;
    private Workout workout;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        ViewModelProviders.of(this).get(EditWorkoutViewModel.class);
        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar_workout_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        workout = (Workout) getIntent().getSerializableExtra("workout");
        show(workout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_edit_workout, menu);

        return true;
    }

    public void onFABButtonClick(View view) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        intent.putExtra("exercise", new BasicExercise());
        startActivityForResult(intent, ADD_EXERCISE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_workout) {
            if (workout.getExercises().size() == 0) {
                Context context = getApplicationContext();
                makeText(context, R.string.message_save_workout_without_exercices, LENGTH_SHORT)
                        .show();
            } else {
                EditText nameEditText = findViewById(R.id.edit_text_workout_name);
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

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        return fragment.onOptionsItemSelected(item);
    }

    public void onSelectionChange(int selectionCount) {
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

    public void show(Workout workout) {
        EditWorkoutFragment fragment = EditWorkoutFragment.forWorkout(workout);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("workout")
                .replace(R.id.fragment_content, fragment, null)
                .setTransition(TRANSIT_NONE)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}