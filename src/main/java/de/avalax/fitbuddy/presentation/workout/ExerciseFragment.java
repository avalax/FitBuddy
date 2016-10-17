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
import de.avalax.fitbuddy.presentation.workout.swipe_bar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipe_bar.VerticalProgressbarView;

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
        setProgressBar = (VerticalProgressbarView) view.findViewById(R.id.leftProgressBar);
        exerciseProgressBar = (VerticalProgressbarView) view.findViewById(R.id.rightProgressBar);
        try {
            Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
            int reps = exerciseViewHelper.maxRepsOfExercise(exercise);
            int sets = exerciseViewHelper.setCountOfExercise(exercise);
            setProgressBar.setOnTouchListener(
                    new SwipeBarOnTouchListener(getActivity(), setProgressBar, reps) {
                        @Override
                        public void onFlingEvent(int moved) {
                            try {
                                changeReps(moved);
                            } catch (ResourceException | IOException e) {
                                Log.d("Can't change reps", e.getMessage(), e);
                            }
                        }
                    });

            exerciseProgressBar.setOnTouchListener(
                    new SwipeBarOnTouchListener(getActivity(), exerciseProgressBar, sets) {
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
        int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
        Set set = exercise.getSets().setAtPosition(indexOfCurrentSet);
        double progress = set.getProgress();
        String reps = String.valueOf(set.getReps());
        String maxReps = String.valueOf(set.getMaxReps());
        setProgressBar.updateProgressbar(progress, reps, maxReps);
    }

    private void updateExerciseProgress() throws ResourceException {
        Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
        String currentValue = String.valueOf(exercise.getSets().indexOfCurrentSet() + 1);
        String maxValue = String.valueOf(exercise.getSets().countOfSets());
        exerciseProgressBar.updateProgressbar(exercise.getProgress(), currentValue, maxValue);
    }

    private void updateWorkoutProgress() {
        ((WorkoutActivity) getActivity()).updateWorkoutProgress(exerciseIndex);
    }

    private void updatePage() {
        ((WorkoutActivity) getActivity()).updatePage(exerciseIndex);
    }
}
