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
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutService;
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
    WorkoutService workoutService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    @Inject
    FinishedWorkoutService finishedWorkoutService;

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
            exerciseIndex = workoutService.indexOfCurrentExercise();
            Exercise exercise = workoutService.requestExercise(exerciseIndex);
            int reps = exerciseViewHelper.maxRepsOfExercise(exercise);
            int sets = exerciseViewHelper.setCountOfExercise(exercise);

            initExerciseProgressBar(sets);
            initSetProgressBar(reps);
            initPager(view);
        } catch (ResourceException e) {
            Log.d("Can't create fragment", e.getMessage(), e);
        }
    }

    private void initSetProgressBar(int reps) {
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
    }

    private void initExerciseProgressBar(int sets) {
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
    }

    private void initPager(View view) throws ResourceException {
        view.findViewById(R.id.pager_exercise).setOnTouchListener(
                new PagerOnTouchListener(getActivity()) {
                    @Override
                    protected void onSwipeRight() {
                        if (workoutService.hasPreviousExercise(exerciseIndex)) {
                            exerciseIndex--;
                            changeExercise();
                        }
                    }

                    @Override
                    protected void onSwipeLeft() {
                        if (workoutService.hasNextExercise(exerciseIndex)) {
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
    }

    void finishWorkout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.message_finish_workout)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    try {
                        workoutService.finishCurrentWorkout();
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

    void changeReps(int moved) throws ResourceException, IOException {
        workoutService.addRepsToSet(exerciseIndex, moved);
        updateWorkoutProgress();
        updateExerciseProgress();
        updateSetProgress();
    }

    void moveToSet(int moved) throws ResourceException, IOException {
        workoutService.switchToSet(exerciseIndex, moved);
        updateWorkoutProgress();
        updateExerciseProgress();
        updateSetProgress();
        updatePage();
    }

    private void updateSetProgress() throws ResourceException {
        Exercise exercise = workoutService.requestExercise(exerciseIndex);
        int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
        Set set = exercise.getSets().get(indexOfCurrentSet);
        double progress = set.getProgress();
        String reps = String.valueOf(set.getReps());
        String maxReps = String.valueOf(set.getMaxReps());
        setProgressBar.updateProgressbar(progress, reps, maxReps);
    }

    private void updateExerciseProgress() throws ResourceException {
        Exercise exercise = workoutService.requestExercise(exerciseIndex);
        String currentValue = String.valueOf(exercise.getSets().indexOfCurrentSet() + 1);
        String maxValue = String.valueOf(exercise.getSets().size());
        exerciseProgressBar.updateProgressbar(exercise.getProgress(), currentValue, maxValue);
    }

    private void updateWorkoutProgress() {
        try {
            int workoutProgress = workoutService.workoutProgress(exerciseIndex);
            workoutProgressBar.setProgress(workoutProgress);
        } catch (ResourceException e) {
            Log.d("Can't change progress", e.getMessage(), e);
        }
    }

    private void updatePage() {
        try {
            workoutService.setCurrentExercise(exerciseIndex);
            Exercise exercise = workoutService.requestExercise(exerciseIndex);
            exerciseNameTextView.setText(exerciseViewHelper.exerciseName(exercise));
            exerciseWeightTextView.setText(exerciseViewHelper.weightOfExercise(exercise));
            if (workoutService.hasPreviousExercise(exerciseIndex)) {
                exercisePreviousTextView.setVisibility(View.VISIBLE);
                Exercise prevExercise = workoutService.requestExercise(exerciseIndex - 1);
                String previousName = exerciseViewHelper.cutPreviousExerciseName(prevExercise);
                exercisePreviousTextView.setText(previousName);
            } else {
                exercisePreviousTextView.setVisibility(View.INVISIBLE);
            }
            if (workoutService.hasNextExercise(exerciseIndex)) {
                exerciseNextTextView.setVisibility(View.VISIBLE);
                Exercise nextExercise = workoutService.requestExercise(exerciseIndex + 1);
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
