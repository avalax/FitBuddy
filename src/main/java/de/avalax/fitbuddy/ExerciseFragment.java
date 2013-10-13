package de.avalax.fitbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import roboguice.fragment.RoboFragment;

public class ExerciseFragment extends RoboFragment {

    private static final int SET_TENDENCY_ON_RETURN = 1;
    public static final int NEXT_SET = 1;

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
        setSet(workout.getExercise(exercisePosition).getSetNumber() + 1);
    }

    private void setReps(int moved) {
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        try {
            workout.getExercise(exercisePosition).setCurrentSet(setNumber);
            setViews(exercisePosition);
        } catch (SetNotAvailableException snae) {
            if (workout.getExercise(exercisePosition).getTendency() == null) {
                startTendencyActivity(exercisePosition);
            } else {
                //TODO: change to the next fragment
            }
        }
    }

    private void startTendencyActivity(int exercisePosition) {
        Intent intent = new Intent(this.getActivity().getApplicationContext(), TendencyActivity.class);
        intent.putExtra("exercise", workout.getExercise(exercisePosition));
        startActivityForResult(intent, SET_TENDENCY_ON_RETURN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode > 0) {
            if (requestCode == SET_TENDENCY_ON_RETURN) {
                Tendency tendency = (Tendency) data.getSerializableExtra("tendency");
                try {
                    workout.setTendency(exercisePosition, tendency);
                    //TODO: change to the next fragment
                } catch (ExerciseNotAvailableException e) {
                    //TODO: change to the next fragment (resultFragment)
                }
            }
        }
    }

    private void setViews(int exercisePosition) {
        fitBuddyProgressBarReps.setProgressBar(workout.getExercise(exercisePosition).getCurrentSet());
        fitBuddyProgressBarSets.setProgressBar(workout.getExercise(exercisePosition));
    }
}
