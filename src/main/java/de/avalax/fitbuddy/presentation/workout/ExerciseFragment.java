package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.workout.swipe_bar.PagerOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipe_bar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipe_bar.VerticalProgressbarView;

public class ExerciseFragment extends Fragment {

    private VerticalProgressbarView setProgressBar;
    private VerticalProgressbarView exerciseProgressBar;
    private ProgressBar workoutProgressBar;
    private TextView exerciseNameTextView;
    private TextView exerciseWeightTextView;
    private TextView exercisePreviousTextView;
    private TextView exerciseNextTextView;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    @Inject
    FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    int exerciseIndex;

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
        exerciseNameTextView = view.findViewById(R.id.exercises_bar_exercise_name);
        exerciseWeightTextView = view.findViewById(R.id.exercises_bar_set_weight);
        exercisePreviousTextView = view.findViewById(R.id.exercises_bar_previous_exercise_name);
        exerciseNextTextView = view.findViewById(R.id.exercises_bar_next_exercise_name);

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

            view.findViewById(R.id.pager_exercise)
                    .setOnTouchListener(new PagerOnTouchListener(getActivity()) {
                        @Override
                        protected void onSwipeRight() {
                            if (workoutApplicationService.hasPreviousExercise(exerciseIndex)) {
                                exerciseIndex--;
                                changeExercise();
                            }
                        }

                        @Override
                        protected void onSwipeLeft() {
                            if (workoutApplicationService.hasNextExercise(exerciseIndex)) {
                                exerciseIndex++;
                                changeExercise();
                            } else {
                                finishWorkout();
                            }
                        }
                    });

            updateExerciseProgress();
            updateSetProgress();
            updatePage();
        } catch (ResourceException e) {
            Log.d("Can't create fragment", e.getMessage(), e);
        }
    }

    void finishWorkout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.message_finish_workout)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    try {
                        workoutApplicationService.finishCurrentWorkout();
                        ((MainActivity) getActivity()).updateBottomNavigation();
                        ((MainActivity) getActivity()).showSummary();
                    } catch (ResourceException e) {
                        Log.d("Can't finish workout", e.getMessage(), e);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    void changeExercise() {
        updatePage();
        try {
            updateSetProgress();
            updateExerciseProgress();
        } catch (ResourceException e) {
            Log.d("Can't change exercise", e.getMessage(), e);
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
            Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
            String name = exercise.getName() + ":";
            exerciseNameTextView.setText(exerciseViewHelper.exerciseName(exercise));
            exerciseWeightTextView.setText(exerciseViewHelper.weightOfExercise(exercise));
            if (workoutApplicationService.hasPreviousExercise(exerciseIndex)) {
                exercisePreviousTextView.setVisibility(View.VISIBLE);
                Exercise prevExercise = workoutApplicationService.requestExercise(exerciseIndex - 1);
                String previousName = exerciseViewHelper.cutPreviousExerciseName(prevExercise);
                exercisePreviousTextView.setText(previousName);
            } else {
                exercisePreviousTextView.setVisibility(View.INVISIBLE);
            }
            if (workoutApplicationService.hasNextExercise(exerciseIndex)) {
                exerciseNextTextView.setVisibility(View.VISIBLE);
                Exercise nextExercise = workoutApplicationService.requestExercise(exerciseIndex + 1);
                String nextName = exerciseViewHelper.cutNextExerciseName(nextExercise);
                exerciseNextTextView.setText(nextName);
            } else {
                exerciseNextTextView.setVisibility(View.INVISIBLE);
            }
            updateWorkoutProgress();
        } catch (ResourceException e) {
            Log.d("can't update page", e.getMessage(), e);
        }
    }
}
