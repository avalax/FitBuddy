package de.avalax.fitbuddy.presentation.edit.exerciseold;

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
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditNameDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditSetsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;

public class EditExerciseDialogFragment extends Fragment {

    private static final String ARGS_EXERCISE = "exercise";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_edit, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init(view);
        return view;
    }

    protected void init(View view) {
        String reps = String.valueOf(exerciseViewHelper.maxRepsOfExercise(exercise));
        String sets = String.valueOf(exerciseViewHelper.setCountOfExercise(exercise));
        String name = exerciseViewHelper.nameOfExercise(exercise);
        String weight = exerciseViewHelper.weightOfExercise(exercise);
        TextView exerciseNameEditText = (TextView) view.findViewById(R.id.exerciseNameEditText);
        TextView exerciseWeightExitText = (TextView) view.findViewById(R.id.exerciseWeightExitText);
        TextView exerciseSetsTextView = (TextView) view.findViewById(R.id.exerciseSetsTextView);
        TextView exerciseRepsTextView = (TextView) view.findViewById(R.id.exerciseRepsTextView);

        exerciseNameEditText.setText(name);
        exerciseWeightExitText.setText(weight);
        exerciseRepsTextView.setText(reps);
        exerciseSetsTextView.setText(sets);

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
            int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
            double weight = exercise.getSets().get(indexOfCurrentSet).getWeight();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_weight");
        } catch (SetException e) {
            Log.d("can't edit weight", e.getMessage(), e);
        }
    }

    private void changeSets() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int sets = exercise.getSets().size();
        EditSetsDialogFragment.newInstance(sets).show(fm, "fragment_edit_sets");
    }

    private void changeReps() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        try {
            int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
            int reps = exercise.getSets().get(indexOfCurrentSet).getMaxReps();
            EditRepsDialogFragment.newInstance(reps).show(fm, "fragment_edit_reps");
        } catch (SetException e) {
            Log.d("can't change reps", e.getMessage(), e);
        }
    }
}
