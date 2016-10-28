package de.avalax.fitbuddy.presentation.edit.exercise;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditSetsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;

public class EditExerciseActivity extends FragmentActivity implements
        EditWeightDialogFragment.DialogListener,
        EditSetsDialogFragment.DialogListener,
        EditRepsDialogFragment.DialogListener,
        EditNameDialogFragment.DialogListener {

    @Inject
    protected EditWorkoutApplicationService editWorkoutApplicationService;

    private Exercise exercise;

    private int position;

    private EditExerciseDialogFragment editExerciseDialogFragment;

    private WorkoutId workoutId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
        workoutId = (WorkoutId) getIntent().getSerializableExtra("workoutId");
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
        if (item.getItemId() == R.id.action_save_exercise) {
            editWorkoutApplicationService.saveExercise(workoutId, exercise, position);
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        double weight = editWeightDialogFragment.getWeight();
        int countOfSets = exercise.getSets().size();
        for (int i = 0; i < countOfSets; i++) {
            try {
                Set set = exercise.getSets().get(i);
                set.setWeight(weight);
            } catch (SetException e) {
                Log.d("can't update weight", e.getMessage(), e);
            }
        }
        editExerciseDialogFragment.init(editExerciseDialogFragment.getView());
    }

    @Override
    public void onDialogPositiveClick(EditSetsDialogFragment editSetsDialogFragment) {
        int newSetAmount = editSetsDialogFragment.getSets();
        try {
            editWorkoutApplicationService.changeSetAmount(exercise, newSetAmount);
        } catch (ResourceException e) {
            Log.d("can't update set amount", e.getMessage(), e);
        }
        editExerciseDialogFragment.init(editExerciseDialogFragment.getView());
    }

    @Override
    public void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment) {
        int reps = editRepsDialogFragment.getReps();
        int countOfSets = exercise.getSets().size();
        for (int i = 0; i < countOfSets; i++) {
            try {
                Set set = exercise.getSets().get(i);
                set.setMaxReps(reps);
            } catch (SetException e) {
                Log.d("can't update max reps", e.getMessage(), e);
            }
        }
        editExerciseDialogFragment.init(editExerciseDialogFragment.getView());
    }

    @Override
    public void onDialogPositiveClick(EditNameDialogFragment editNameDialogFragment) {
        String name = editNameDialogFragment.getName();
        exercise.setName(name);
        editExerciseDialogFragment.init(editExerciseDialogFragment.getView());
        getActionBar().setTitle(exercise.getName());
    }
}