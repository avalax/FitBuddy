package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.progressBar.ProgressBarOnTouchListener;
import de.avalax.fitbuddy.progressBar.VerticalProgressBar;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ExerciseFragment extends RoboFragment {

    @Inject
    private Workout workout;
    @InjectView(R.id.repsProgressBar)
    private VerticalProgressBar repsProgressBar;
    @InjectView(R.id.setsProgressBar)
    private VerticalProgressBar setsProgressBar;
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

        repsProgressBar.setOnTouchListener(new ProgressBarOnTouchListener(repsProgressBar, getMaxMoveForReps(workout)) {
            @Override
            public void onGestureFlingEvent(int moved) {
                changeReps(moved);
            }
        });

        setsProgressBar.setOnTouchListener(new ProgressBarOnTouchListener(setsProgressBar, getMaxMoveForSets(workout)) {
            @Override
            public void onGestureFlingEvent(int moved) {
                changeSets(moved);
            }
        });

        setViews(exercisePosition);
    }

    private int getMaxMoveForSets(Workout workout) {
        return workout.getExercise(exercisePosition).getMaxSets();
    }

    private int getMaxMoveForReps(Workout workout) {
        return workout.getCurrentSet(exercisePosition).getMaxReps();
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
        repsProgressBar.setProgressBar(workout.getExercise(exercisePosition).getCurrentSet());
        setsProgressBar.setProgressBar(workout.getExercise(exercisePosition));
    }
}
