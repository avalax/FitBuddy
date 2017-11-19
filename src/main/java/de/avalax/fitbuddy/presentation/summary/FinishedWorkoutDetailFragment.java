package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class FinishedWorkoutDetailFragment extends Fragment {
    private static final String ARGS_WORKOUT_ID = "finished_workout_id";
    private TextView nameTextView;

    @Inject
    FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    public static FinishedWorkoutDetailFragment newInstance(FinishedWorkoutId finishedWorkoutId) {
        FinishedWorkoutDetailFragment fragment = new FinishedWorkoutDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_WORKOUT_ID, finishedWorkoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_finished_workout, container, false);
        nameTextView = view.findViewById(R.id.finished_workout_name);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FinishedWorkoutId finishedWorkoutId
                = (FinishedWorkoutId) getArguments().getSerializable(ARGS_WORKOUT_ID);

        try {
            FinishedWorkout finishedWorkout = finishedWorkoutApplicationService.load(finishedWorkoutId);
            nameTextView.setText(finishedWorkout.getName());
        } catch (FinishedWorkoutException e) {
            Log.d("Can't load workout", e.getMessage(), e);
        }
    }
}
