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

import java.text.DecimalFormat;

public class EditExerciseDialogFragment extends Fragment {

    private static final String ARGS_EXERCISE = "exercise";

    private DecimalFormat decimalFormat;

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
        this.decimalFormat = new DecimalFormat("###.#");
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init();
        return view;
    }

    protected void init() {
        //TODO: 3x times - unnamed exercise from resources & move to a ui helper
        exerciseNameEditText.setText(exercise.getName().length() > 0 ? exercise.getName() : "unnamed exercise");
        exerciseWeightExitText.setText(getWeightText(exercise));
        exerciseRepsTextView.setText(getMaxRepsText(exercise));
        exerciseSetsTextView.setText(getSetsText(exercise));
    }

    private String getSetsText(Exercise exercise) {
        return exercise.getSets().isEmpty() ? "0" : String.valueOf(exercise.getSets().size());
    }

    private String getMaxRepsText(Exercise exercise) {
        return exercise.getSets().isEmpty() ? "0" : String.valueOf(exercise.getCurrentSet().getMaxReps());
    }

    private String getWeightText(Exercise exercise) {
        if (exercise.getSets().isEmpty()) {
            return "-";
        }
        double weight = exercise.getCurrentSet().getWeight();
        if (weight > 0) {
            return decimalFormat.format(weight);
        } else {
            return "-";
        }
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
        if (exercise.getSets().isEmpty()) {
            return;
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        double weight = exercise.getCurrentSet().getWeight();
        EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
    }

    @OnClick(R.id.exerciseSets)
    protected void changeSets() {
        //TODO: better fallback
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int sets = exercise.getSets().size();
        EditSetsDialogFragment.newInstance(sets).show(fm, "fragment_edit_sets");
    }

    @OnClick(R.id.exerciseReps)
    protected void changeReps() {
        //TODO: better fallback
        if (exercise.getSets().isEmpty()) {
            return;
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int reps = exercise.getCurrentSet().getMaxReps();
        EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_reps");
    }
}
