package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.fragment.RoboFragment;

public class ExerciseFragment extends RoboFragment {

    @Inject
    private Workout workout;
    private int exercisePosition;
    private FitBuddyProgressBar fitBuddyProgressBarReps;
    private FitBuddyProgressBar fitBuddyProgressBarSets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int exerciseIndex = getArguments().getInt("exerciseIndex");
        View rootView = inflater.inflate(R.layout.fragment_exercise_object, container, false);
        fitBuddyProgressBarReps = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarReps);
        fitBuddyProgressBarSets = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarSets);
        exercisePosition = exerciseIndex;

        setViews(exercisePosition);

        fitBuddyProgressBarReps.setOnTouchListener(new ProgressBarGestureListener() {
            @Override
            public void onFlingEvent(int moved) {
                changeReps(moved);
            }
        });

        fitBuddyProgressBarSets.setOnTouchListener(new ProgressBarGestureListener() {
            @Override
            public void onFlingEvent(int moved) {
                changeSets(moved);
            }
        });

        return rootView;
    }

    private void changeReps(int moved) {
        setReps(moved);
        setViews(exercisePosition);
    }

    private void changeSets(int moved) {
        setSet(workout.getExercise(exercisePosition).getSetNumber() + moved);
    }

    private void setReps(int moved) {
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        workout.getExercise(exercisePosition).setCurrentSet(setNumber);
        setViews(exercisePosition);
    }

    private void setViews(int exercisePosition) {
        fitBuddyProgressBarReps.setProgressBar(workout.getExercise(exercisePosition).getCurrentSet());
        fitBuddyProgressBarSets.setProgressBar(workout.getExercise(exercisePosition));
    }
}
