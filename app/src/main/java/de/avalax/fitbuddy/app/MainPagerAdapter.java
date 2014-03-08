package de.avalax.fitbuddy.app;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import java.text.DecimalFormat;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final int ADDITIONAL_FRAGMENTS = 1;
    protected WorkoutSession workoutSession;
    private String resultTitle;
    private String exerciseTitle;
    DecimalFormat decimalFormat;

    public MainPagerAdapter(FragmentManager fm, Context context, WorkoutSession workoutSession) {
        super(fm);
        this.workoutSession = workoutSession;
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.resultTitle = resources.getString(R.string.title_result);
        this.exerciseTitle = resources.getString(R.string.title_exercise_with_weight);
        this.decimalFormat = new DecimalFormat("###.#");
    }

    @Override
    public Fragment getItem(int position) {
        Workout workout = workoutSession.getWorkout();
        if (position - workout.getExerciseCount() == 0) {
            return new ResultChartFragment();
        } else {
            return CurrentExerciseFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        Workout workout = workoutSession.getWorkout();
        return workout.getExerciseCount() + ADDITIONAL_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Workout workout = workoutSession.getWorkout();
        if (position - workout.getExerciseCount() == 0) {
            return String.format(resultTitle,workout.getName());
        }
        else {
            Exercise currentExercise = workout.getExercise(position);
            double weight = currentExercise.getWeight();
            return getWeightText(currentExercise, weight);
        }
    }

    private CharSequence getWeightText(Exercise exercise, double weight) {
        if (weight > 0) {
            return String.format(exerciseTitle, exercise.getName(), decimalFormat.format(weight));
        } else {
            return exercise.getName();
        }
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
