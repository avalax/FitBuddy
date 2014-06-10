package de.avalax.fitbuddy.app.manageWorkout.editExercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.ButterKnife;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkout;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;

public class EditExerciseActivity extends FragmentActivity {

    @Inject
    protected ManageWorkout manageWorkout;

    private Exercise exercise;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        Fragment fragment = EditExerciseDialogFragment.newInstance(exercise);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
        getActionBar().setTitle(exercise.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_exercise_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_exercise) {
            manageWorkout.deleteExercise(exercise);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_save_exercise) {
            manageWorkout.replaceExercise(exercise);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_exercise) {
            manageWorkout.createExerciseBefore(exercise);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_exercise_after) {
            manageWorkout.createExerciseAfter(exercise);
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}