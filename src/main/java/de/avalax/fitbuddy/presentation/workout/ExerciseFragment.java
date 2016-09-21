package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.workout.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipeBar.VerticalProgressbarView;

public class ExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    private VerticalProgressbarView setProgressBar;
    private VerticalProgressbarView exerciseProgressBar;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    private int exerciseIndex;

    public static ExerciseFragment newInstance(int exerciseIndex) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_INDEX, exerciseIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        exerciseIndex = getArguments().getInt(ARGS_EXERCISE_INDEX);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setProgressBar = (VerticalProgressbarView) getView().findViewById(R.id.leftProgressBar);
        exerciseProgressBar = (VerticalProgressbarView) getView().findViewById(R.id.rightProgressBar);
        try {
            Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
            setProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(getActivity(), setProgressBar, exerciseViewHelper.maxRepsOfExercise(exercise)) {
                @Override
                public void onFlingEvent(int moved) {
                    try {
                        changeReps(moved);
                    } catch (ResourceException | IOException e) {
                        Log.d("Can't change reps", e.getMessage(), e);
                    }
                }
            });

            exerciseProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(getActivity(), exerciseProgressBar, exerciseViewHelper.setCountOfExercise(exercise)) {
                @Override
                public void onFlingEvent(int moved) {
                    try {
                        moveToSet(moved);
                    } catch (ResourceException | IOException e) {
                        Log.d("Can't change set", e.getMessage(), e);
                    }
                }
            });

            updateExerciseProgress();
            updateSetProgress();
        } catch (ResourceException e) {
            Log.d("Can't create fragment", e.getMessage(), e);
        }
    }

    private void changeReps(int moved) throws ResourceException, IOException {
        workoutApplicationService.addRepsToSet(exerciseIndex, moved);
        updateWorkoutProgress();
        updateExerciseProgress();
        updateSetProgress();
    }

    private void moveToSet(int moved) throws ResourceException, IOException {
        workoutApplicationService.switchToSet(exerciseIndex, moved);
        updateWorkoutProgress();
        updateExerciseProgress();
        updateSetProgress();
        updatePage();
    }

    private void updateSetProgress() throws ResourceException {
        Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(indexOfCurrentSet);
        setProgressBar.updateProgressbar(set.getProgress(), String.valueOf(set.getReps()), String.valueOf(set.getMaxReps()));
    }

    private void updateExerciseProgress() throws ResourceException {
        Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
        String currentValueText = String.valueOf(exercise.indexOfCurrentSet() + 1);
        String maxValueText = String.valueOf(exercise.countOfSets());
        exerciseProgressBar.updateProgressbar(exercise.getProgress(), currentValueText, maxValueText);
    }

    private void updateWorkoutProgress() {
        ((WorkoutActivity) getActivity()).updateWorkoutProgress(exerciseIndex);
    }

    private void updatePage() {
        ((WorkoutActivity) getActivity()).updatePage(exerciseIndex);
    }
}
