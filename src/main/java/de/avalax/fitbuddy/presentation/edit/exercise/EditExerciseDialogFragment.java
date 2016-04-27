package de.avalax.fitbuddy.presentation.edit.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditSetsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;

public class EditExerciseDialogFragment extends Fragment {

    private static final String ARGS_EXERCISE = "exercise";

    @BindView(R.id.exerciseNameEditText)
    protected TextView exerciseNameEditText;

    @BindView(R.id.exerciseWeightExitText)
    protected TextView exerciseWeightExitText;

    @BindView(R.id.exerciseSetsTextView)
    protected TextView exerciseSetsTextView;

    @BindView(R.id.exerciseRepsTextView)
    protected TextView exerciseRepsTextView;

    @Inject
    ExerciseViewHelper exerciseViewHelper;

    private Exercise exercise;

    public static EditExerciseDialogFragment newInstance(Exercise exercise) {
        EditExerciseDialogFragment fragment = new EditExerciseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_EXERCISE, exercise);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_edit, container, false);
        ButterKnife.bind(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init();
        return view;
    }

    protected void init() {
        exerciseNameEditText.setText(exerciseViewHelper.nameOfExercise(exercise));
        exerciseWeightExitText.setText(exerciseViewHelper.weightOfExercise(exercise));
        exerciseRepsTextView.setText(String.valueOf(exerciseViewHelper.maxRepsOfExercise(exercise)));
        exerciseSetsTextView.setText(String.valueOf(exerciseViewHelper.setCountOfExercise(exercise)));
    }


    @OnClick(R.id.exerciseName)
    protected void changeName() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String name = exercise.getName();
        String hint = getResources().getString(R.string.new_exercise_name);
        EditNameDialogFragment.newInstance(name, hint).show(fm, "fragment_edit_name");
    }


    @OnClick(R.id.exerciseWeight)
    protected void changeWeight() {
        try {
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            double weight = exercise.setAtPosition(indexOfCurrentSet).getWeight();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
        } catch (SetNotFoundException e) {
            Log.d("can't edit weight", e.getMessage(), e);
        }
    }

    @OnClick(R.id.exerciseSets)
    protected void changeSets() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int sets = exercise.countOfSets();
        EditSetsDialogFragment.newInstance(sets).show(fm, "fragment_edit_sets");
    }

    @OnClick(R.id.exerciseReps)
    protected void changeReps() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        try {
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            int reps = exercise.setAtPosition(indexOfCurrentSet).getMaxReps();
            EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_reps");
        } catch (SetNotFoundException e) {
            Log.d("can't change reps", e.getMessage(), e);
        }
    }
}
