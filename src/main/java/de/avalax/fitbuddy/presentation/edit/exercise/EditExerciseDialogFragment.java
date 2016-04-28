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

    private TextView exerciseNameEditText;

    private TextView exerciseWeightExitText;

    private TextView exerciseSetsTextView;

    private TextView exerciseRepsTextView;

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
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init(view);
        return view;
    }

    protected void init(View view) {
        exerciseNameEditText = (TextView) view.findViewById(R.id.exerciseNameEditText);
        exerciseWeightExitText = (TextView) view.findViewById(R.id.exerciseWeightExitText);
        exerciseSetsTextView = (TextView) view.findViewById(R.id.exerciseSetsTextView);
        exerciseRepsTextView = (TextView) view.findViewById(R.id.exerciseRepsTextView);

        exerciseNameEditText.setText(exerciseViewHelper.nameOfExercise(exercise));
        exerciseWeightExitText.setText(exerciseViewHelper.weightOfExercise(exercise));
        exerciseRepsTextView.setText(String.valueOf(exerciseViewHelper.maxRepsOfExercise(exercise)));
        exerciseSetsTextView.setText(String.valueOf(exerciseViewHelper.setCountOfExercise(exercise)));

        view.findViewById(R.id.exerciseName).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeName();
            }
        });

        view.findViewById(R.id.exerciseWeight).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeWeight();
            }
        });

        view.findViewById(R.id.exerciseSets).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeSets();
            }
        });

        view.findViewById(R.id.exerciseReps).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeReps();
            }
        });
    }

    private void changeName() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String name = exercise.getName();
        String hint = getResources().getString(R.string.new_exercise_name);
        EditNameDialogFragment.newInstance(name, hint).show(fm, "fragment_edit_name");
    }

    private void changeWeight() {
        try {
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            double weight = exercise.setAtPosition(indexOfCurrentSet).getWeight();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
        } catch (SetNotFoundException e) {
            Log.d("can't edit weight", e.getMessage(), e);
        }
    }

    private void changeSets() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int sets = exercise.countOfSets();
        EditSetsDialogFragment.newInstance(sets).show(fm, "fragment_edit_sets");
    }

    private void changeReps() {
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
