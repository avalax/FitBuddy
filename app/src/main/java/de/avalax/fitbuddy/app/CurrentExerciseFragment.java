package de.avalax.fitbuddy.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.VerticalProgressbarView;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class CurrentExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    @InjectView(R.id.leftProgressBar)
    protected VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    protected VerticalProgressbarView setsProgressBar;
    @InjectView(R.id.workoutProgressBar)
    protected ProgressBar workoutProggressBar;
    @Inject
    protected WorkoutSession workoutSession;
    private int exercisePosition;
    private ViewPager viewPager;

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
        this.exercisePosition = getArguments().getInt(ARGS_EXERCISE_INDEX);
        this.viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        updateWorkoutProgress(exercisePosition);
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

        setViews(exercisePosition);
    }

    private int getMaxMoveForSets(Workout workout) {
        return workout.getExercise(exercisePosition).getMaxSets();
    }

    private int getMaxMoveForReps(Workout workout) {
        return workout.getExercise(exercisePosition).getMaxReps();
    }

    private void changeReps(int moved) {
        setReps(moved);
        setViews(exercisePosition);
        //TODO: only update ResultChartFragment
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.invalidate();
    }

    private void changeSets(int moved) {
        Workout workout = workoutSession.getWorkout();
        setSet(workout.getExercise(exercisePosition).getSetNumber() + moved);
        setViews(exercisePosition);
    }

    private void setReps(int moved) {
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        Workout workout = workoutSession.getWorkout();
        workout.getExercise(exercisePosition).setCurrentSet(setNumber);
    }

    private void setViews(int exercisePosition) {
        Workout workout = workoutSession.getWorkout();
        repsProgressBar.updateProgressbar(workout.getExercise(exercisePosition).getCurrentSet());
        setsProgressBar.updateProgressbar(workout.getExercise(exercisePosition));
        updateWorkoutProgress(exercisePosition);
    }

    private void updateWorkoutProgress(int exercisePosition) {
        Workout workout = workoutSession.getWorkout();
        float currentValue = workout.getProgress(exercisePosition);
        int maxValue = workout.getExerciseCount();
        workoutProggressBar.setProgress(calculateProgressbarHeight(currentValue,maxValue));
    }

    private int calculateProgressbarHeight(float currentValue, int maxValue) {
        float scale = currentValue / maxValue;
        return Math.round(scale * 100);
    }
}
