package de.avalax.fitbuddy.app.manageWorkout.editExercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.otto.Bus;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseChangedEvent;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseDeletedEvent;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;

public class EditExerciseDialogFragment extends Fragment {

    private static final String ARGS_EXERCISE = "exercise";

    @Inject
    protected Bus bus;

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
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        ButterKnife.inject(this, view);
        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        init();
        return view;
    }

    private void init() {
        exerciseNameEditText.setText(exercise.getName());
        exerciseWeightExitText.setText(String.valueOf(exercise.getWeight()));
        exerciseRepsTextView.setText(String.valueOf(exercise.getMaxReps()));
        exerciseSetsTextView.setText(String.valueOf(exercise.getMaxSets()));
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private void onDelete() {
        bus.post(new ExerciseDeletedEvent(exercise));
    }

    private void onReturn() {
        bus.post(new ExerciseChangedEvent(exercise));
    }
}
