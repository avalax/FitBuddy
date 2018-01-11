package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

public class FinishedWorkoutDetailFragment extends Fragment {
    public static final String ARGS_FINISHED_WORKOUT = "workout";

    @Inject
    FinishedWorkoutService finishedWorkoutService;

    @Inject
    FinishedWorkoutViewHelper finishedWorkoutViewHelper;

    @Inject
    FinishedExerciseViewHelper finishedExerciseViewHelper;

    private TextView createdTextView;
    private TextView executedTextView;
    private FinishedWorkout finishedWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_finished_workout, container, false);
        createdTextView = view.findViewById(R.id.finished_workout_created);
        executedTextView = view.findViewById(R.id.finished_workout_executed);
        finishedWorkout = (FinishedWorkout)
                getActivity().getIntent().getSerializableExtra(ARGS_FINISHED_WORKOUT);
        WorkoutRecyclerView recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));

        RecyclerView.Adapter adapter =
                new FinishedExerciseAdapter(
                        getActivity(),
                        finishedExerciseViewHelper,
                        finishedWorkout.getFinishedExercises());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createdTextView.setText(finishedWorkoutViewHelper.creationDate(finishedWorkout));
        executedTextView.setText(finishedWorkoutViewHelper.executions(finishedWorkout.getWorkoutId()));
    }
}
