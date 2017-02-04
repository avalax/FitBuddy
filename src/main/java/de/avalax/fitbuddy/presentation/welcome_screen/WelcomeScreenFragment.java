package de.avalax.fitbuddy.presentation.welcome_screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class WelcomeScreenFragment extends Fragment {
    @Inject
    WorkoutSession workoutSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen,
                container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        init(view);
        return view;
    }

    private void init(View view) {
        View continueWorkoutButton = view.findViewById(R.id.continue_workout_button);
        continueWorkoutButton.setEnabled(workoutSession.hasWorkout());
    }


}
