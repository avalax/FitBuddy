package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

    private VerticalProgressbarView setProgressBar;
    private VerticalProgressbarView exerciseProgressBar;
    private ProgressBar workoutProgressBar;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;

    private int exerciseIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setProgressBar = view.findViewById(R.id.leftProgressBar);
        exerciseProgressBar = view.findViewById(R.id.rightProgressBar);
        workoutProgressBar = view.findViewById(R.id.workoutProgressBar);
        try {
            exerciseIndex = workoutApplicationService.indexOfCurrentExercise();
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
            updateWorkoutProgress();
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
        Set set = exercise.getSets().get(indexOfCurrentSet);
        double progress = set.getProgress();
        String reps = String.valueOf(set.getReps());
        String maxReps = String.valueOf(set.getMaxReps());
        setProgressBar.updateProgressbar(progress, reps, maxReps);
    }

    private void updateExerciseProgress() throws ResourceException {
        Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
        String currentValue = String.valueOf(exercise.getSets().indexOfCurrentSet() + 1);
        String maxValue = String.valueOf(exercise.getSets().size());
        exerciseProgressBar.updateProgressbar(exercise.getProgress(), currentValue, maxValue);
    }

    private void updateWorkoutProgress() {
        try {
            int workoutProgress = workoutApplicationService.workoutProgress(exerciseIndex);
            workoutProgressBar.setProgress(workoutProgress);
        } catch (ResourceException e) {
            Log.d("Can't change progress", e.getMessage(), e);
        }
    }

    private void updatePage() {
        try {
            workoutApplicationService.setCurrentExercise(exerciseIndex);
            updateWorkoutProgress();
            //TODO: Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
            //TODO: setTitle(exerciseViewHelper.nameOfExercise(exercise));
            //TODO: menuItem.setTitle(exerciseViewHelper.weightOfExercise(exercise));
        } catch (ResourceException e) {
            Log.d("can't update page", e.getMessage(), e);
        }
    }
}
