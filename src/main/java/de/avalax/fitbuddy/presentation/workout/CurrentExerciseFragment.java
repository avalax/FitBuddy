package de.avalax.fitbuddy.presentation.workout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.workout.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipeBar.VerticalProgressbarView;

import javax.inject.Inject;

public class CurrentExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    @InjectView(R.id.leftProgressBar)
    protected VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    protected VerticalProgressbarView setsProgressBar;
    @Inject
    protected WorkoutSession workoutSession;
    private int exerciseIndex;

    public static CurrentExerciseFragment newInstance(int exerciseIndex) {
        CurrentExerciseFragment fragment = new CurrentExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_INDEX, exerciseIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.exerciseIndex = getArguments().getInt(ARGS_EXERCISE_INDEX);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Workout workout = workoutSession.getWorkout();
        final Context context = getActivity();
        repsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, repsProgressBar, getMaxMoveForReps(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeReps(moved);
            }
        });

        setsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, setsProgressBar, getMaxMoveForSets(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeSets(moved);
            }
        });

        setViews(exerciseIndex);
    }

    private int getMaxMoveForSets(Workout workout) {
        return workout.getExercises().get(exerciseIndex).getSets().size();
    }

    private int getMaxMoveForReps(Workout workout) {
        Exercise exercise = workout.getExercises().get(exerciseIndex);
        if (exercise.getSets().isEmpty()) {
            return 0;
        }
        return exercise.getCurrentSet().getMaxReps();
    }

    private void changeReps(int moved) {
        setReps(moved);
        setViews(exerciseIndex);
        updateWorkoutProgress(exerciseIndex);
    }

    private void changeSets(int moved) {
        Workout workout = workoutSession.getWorkout();
        setSet(workout.getExercises().get(exerciseIndex).indexOfCurrentSet() + moved);
        setViews(exerciseIndex);
        updateWorkoutProgress(exerciseIndex);
        updatePage();
    }

    private void setReps(int moved) {
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercises().get(exerciseIndex);
        if (exercise.getSets().isEmpty()) {
            return;
        }
        //TODO: add to Set addRep, removeRep
        exercise.getCurrentSet().setReps(exercise.getCurrentSet().getReps() + moved);
    }

    private void setSet(int setNumber) {
        Workout workout = workoutSession.getWorkout();
        workout.getExercises().get(exerciseIndex).setCurrentSet(setNumber);
    }

    private void setViews(int exerciseIndex) {
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercises().get(exerciseIndex);
        if (exercise.getSets().size() > 0) {
            repsProgressBar.updateProgressbar(exercise.getCurrentSet());
        }
        setsProgressBar.updateProgressbar(exercise);
    }

    private void updateWorkoutProgress(int exerciseIndex) {
        ((MainActivity) getActivity()).updateWorkoutProgress(exerciseIndex);
    }

    private void updatePage() {
        ((MainActivity) getActivity()).updatePage(exerciseIndex);
    }
}
