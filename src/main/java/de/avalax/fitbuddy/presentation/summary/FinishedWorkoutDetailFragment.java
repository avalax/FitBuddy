package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class FinishedWorkoutDetailFragment extends Fragment {
    public static final String ARGS_FINISHED_WORKOUT = "workout";

    @Inject
    FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    @Inject
    FinishedWorkoutViewHelper finishedWorkoutViewHelper;

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
        finishedWorkout = (FinishedWorkout) getActivity().getIntent().getSerializableExtra(ARGS_FINISHED_WORKOUT);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        createdTextView.setText(finishedWorkoutViewHelper.creationDate(finishedWorkout));
        executedTextView.setText(finishedWorkoutViewHelper.executions(finishedWorkout));
    }
}
