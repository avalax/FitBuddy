package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class WorkoutFragment extends Fragment {

    @Inject
    WorkoutApplicationService workoutApplicationService;

    private TextView exerciseNameTextView;
    private TextView exerciseWeightTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        exerciseNameTextView = view.findViewById(R.id.exercises_bar_name);
        exerciseWeightTextView = view.findViewById(R.id.exercises_bar_weight);
        try {
            exerciseNameTextView.setText(workoutApplicationService.requestExercise(0).getName());
            exerciseWeightTextView.setText(workoutApplicationService.weightOfCurrentSet(0) + " kg");

        } catch (ResourceException e) {
            Log.e("ResourceException", e.getMessage(), e);
        }

        return view;
    }
}
