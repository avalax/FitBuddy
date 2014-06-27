package de.avalax.fitbuddy.application.manageWorkout.editExercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.avalax.fitbuddy.application.R;
import de.avalax.fitbuddy.application.dialog.EditNameDialogFragment;
import de.avalax.fitbuddy.application.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.application.dialog.EditSetsDialogFragment;
import de.avalax.fitbuddy.application.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

public class EditExerciseDialogFragment extends Fragment {

    private static final String ARGS_EXERCISE = "exercise";

    @InjectView(R.id.exerciseNameEditText)
    protected TextView exerciseNameEditText;

    @InjectView(R.id.exerciseWeightExitText)
    protected TextView exerciseWeightExitText;

    @InjectView(R.id.exerciseSetsTextView)
    protected TextView exerciseSetsTextView;

    @InjectView(R.id.exerciseRepsTextView)
    protected TextView exerciseRepsTextView;

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
        ButterKnife.inject(this, view);
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init();
        return view;
    }

    protected void init() {
        //TODO: 3x times - unnamed exercise from resources & move to a ui helper
        exerciseNameEditText.setText(exercise.getName().length() > 0 ? exercise.getName() : "unnamed exercise");
        exerciseWeightExitText.setText(String.valueOf(exercise.getCurrentSet().getWeight()));
        exerciseRepsTextView.setText(String.valueOf(exercise.getMaxReps()));
        exerciseSetsTextView.setText(String.valueOf(exercise.getSets().size()));
    }

    @OnClick(R.id.exerciseName)
    protected void changeName() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String name = exercise.getName();
        EditNameDialogFragment.newInstance(name).show(fm, "fragment_edit_name");
    }


    @OnClick(R.id.exerciseWeight)
    protected void changeWeight() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        double weight = exercise.getCurrentSet().getWeight();
        EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
    }

    @OnClick(R.id.exerciseSets)
    protected void changeSets() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int sets = exercise.getSets().size();
        EditSetsDialogFragment.newInstance(sets).show(fm, "fragment_edit_sets");
    }

    @OnClick(R.id.exerciseReps)
    protected void changeReps() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int reps = exercise.getMaxReps();
        EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_reps");
    }
}
