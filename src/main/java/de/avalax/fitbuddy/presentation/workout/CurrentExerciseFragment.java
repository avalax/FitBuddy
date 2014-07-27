package de.avalax.fitbuddy.presentation.workout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.workout.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipeBar.VerticalProgressbarView;

import javax.inject.Inject;

public class CurrentExerciseFragment extends Fragment {

    private static final String ARGS_WORKOUT_ID = "workoutId";
    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    @InjectView(R.id.leftProgressBar)
    protected VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    protected VerticalProgressbarView setsProgressBar;
    @Inject
    protected WorkoutApplicationService workoutApplicationService;
    private Exercise exercise;
    private int exerciseIndex;

    public static CurrentExerciseFragment newInstance(String workoutId, int exerciseIndex) {
        CurrentExerciseFragment fragment = new CurrentExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_INDEX, exerciseIndex);
        args.putString(ARGS_WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getActivity();
        exerciseIndex = getArguments().getInt(ARGS_EXERCISE_INDEX);
        String workoutId = getArguments().getString(ARGS_WORKOUT_ID);
        try {
            this.exercise = workoutApplicationService.exerciseFromPosition(workoutId, exerciseIndex);
            repsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, repsProgressBar, getMaxMoveForReps(exercise)) {
                @Override
                public void onFlingEvent(int moved) {
                    changeReps(moved);
                }
            });

            setsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, setsProgressBar, getMaxMoveForSets(exercise)) {
                @Override
                public void onFlingEvent(int moved) {
                    changeSets(moved);
                }
            });

            setViews();
        } catch (ExerciseNotFoundException e) {
            Log.d("Can't create fragment", e.getMessage(), e);
        }
    }

    private int getMaxMoveForSets(Exercise exercise) {
        return exercise.getSets().size();
    }

    private int getMaxMoveForReps(Exercise exercise) {
        if (exercise.getSets().isEmpty()) {
            return 0;
        }
        return exercise.getCurrentSet().getMaxReps();
    }

    private void changeReps(int moved) {
        setReps(moved);
        setViews();
        updateWorkoutProgress();
    }

    private void changeSets(int moved) {
        setSet(exercise.indexOfCurrentSet() + moved);
        setViews();
        updateWorkoutProgress();
        updatePage();
    }

    private void setReps(int moved) {
        if (exercise.getSets().isEmpty()) {
            return;
        }
        //TODO: add to Set addRep, removeRep
        exercise.getCurrentSet().setReps(exercise.getCurrentSet().getReps() + moved);
    }

    private void setSet(int setNumber) {
        exercise.setCurrentSet(setNumber);
    }

    private void setViews() {
        if (exercise.getSets().size() > 0) {
            repsProgressBar.updateProgressbar(exercise.getCurrentSet());
        }
        setsProgressBar.updateProgressbar(exercise);
    }

    private void updateWorkoutProgress() {
        ((MainActivity) getActivity()).updateWorkoutProgress(exerciseIndex);
    }

    private void updatePage() {
        ((MainActivity) getActivity()).updatePage(exerciseIndex);
    }
}
