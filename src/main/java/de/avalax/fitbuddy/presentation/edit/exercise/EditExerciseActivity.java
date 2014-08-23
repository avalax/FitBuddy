package de.avalax.fitbuddy.presentation.edit.exercise;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.ButterKnife;
import de.avalax.fitbuddy.application.edit.workout.ManageWorkout;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditSetsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;

import javax.inject.Inject;

public class EditExerciseActivity extends FragmentActivity implements EditWeightDialogFragment.DialogListener, EditSetsDialogFragment.DialogListener, EditRepsDialogFragment.DialogListener, EditNameDialogFragment.DialogListener {

    @Inject
    protected ManageWorkout manageWorkout;

    private Exercise exercise;

    private int position;

    private EditExerciseDialogFragment editExerciseDialogFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        position = getIntent().getIntExtra("position", -1);
        editExerciseDialogFragment = EditExerciseDialogFragment.newInstance(exercise);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editExerciseDialogFragment).commit();
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
            manageWorkout.deleteExercise(exercise, position);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_save_exercise) {
            manageWorkout.saveExercise(exercise, position);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_exercise) {
            manageWorkout.createExerciseBefore(position);
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.action_add_exercise_after) {
            manageWorkout.createExerciseAfter(position);
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        double weight = editWeightDialogFragment.getWeight();
        int countOfSets = exercise.countOfSets();
        for (int i = 0; i < countOfSets; i++) {
            try {
                Set set = exercise.setAtPosition(i);
                set.setWeight(weight);
            } catch (SetNotAvailableException e) {
                Log.d("can't update weight", e.getMessage(), e);
            }
        }
        editExerciseDialogFragment.init();
    }

    @Override
    public void onDialogPositiveClick(EditSetsDialogFragment editSetsDialogFragment) {
        int newSetAmount = editSetsDialogFragment.getSets();
        int maxReps = 0;
        double weight = 0;
        try {
            maxReps = exercise.countOfSets() == 0 ? 0 : exercise.setAtPosition(0).getMaxReps();
            weight = exercise.countOfSets() == 0 ? 0 : exercise.setAtPosition(0).getWeight();
        } catch (SetNotAvailableException e) {
            Log.d("can't get first set", e.getMessage(), e);
        }
        manageWorkout.changeSetAmount(exercise, newSetAmount);
        editExerciseDialogFragment.init();
    }

    @Override
    public void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment) {
        int reps = editRepsDialogFragment.getReps();
        int countOfSets = exercise.countOfSets();
        for (int i = 0; i < countOfSets; i++) {
            try {
                Set set = exercise.setAtPosition(i);
                set.setMaxReps(reps);
            } catch (SetNotAvailableException e) {
                Log.d("can't update max reps", e.getMessage(), e);
            }
        }
        editExerciseDialogFragment.init();
    }

    @Override
    public void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment) {
        String name = editNameDialogFragment.getName();
        exercise.setName(name);
        editExerciseDialogFragment.init();
        getActionBar().setTitle(exercise.getName());
    }
}