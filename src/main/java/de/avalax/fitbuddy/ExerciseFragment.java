package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.fragment.RoboFragment;

public class ExerciseFragment extends RoboFragment {

    @Inject
    private Workout workout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_object, container, false);
        Bundle args = getArguments();

        int exerciseIndex = args.getInt("exerciseIndex");

        int maxReps = workout.getCurrentSet(exerciseIndex).getRepsSize();
        int maxSets = workout.getExercise(exerciseIndex).getSetSize();

        FitBuddyProgressBar fitBuddyProgressBarReps = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarReps);
        fitBuddyProgressBarReps.setMaxValue(maxReps);
        FitBuddyProgressBar fitBuddyProgressBarSets = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarSets);
        fitBuddyProgressBarSets.setMaxValue(maxSets);
        fitBuddyProgressBarSets.setCurrentValue(1);


        return rootView;
    }
}
