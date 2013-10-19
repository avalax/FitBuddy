package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ExerciseFragment extends RoboFragment {

    @Inject
    private Workout workout;
    @InjectView(R.id.progressBarReps)
    private FitBuddyProgressBar fitBuddyProgressBarReps;
    @InjectView(R.id.progressBarSets)
    private FitBuddyProgressBar fitBuddyProgressBarSets;
    @Inject
    private WindowManager windowManager;
    private int exercisePosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        exercisePosition = getArguments().getInt("exerciseIndex");
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fitBuddyProgressBarReps.setOnTouchListener(new ProgressBarGestureListener(windowManager) {
            @Override
            public void onFlingEvent(int moved) {
                changeReps(moved);
            }
        });

        fitBuddyProgressBarSets.setOnTouchListener(new ProgressBarGestureListener(windowManager) {
            @Override
            public void onFlingEvent(int moved) {
                changeSets(moved);
            }
        });

        setViews(exercisePosition);
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
