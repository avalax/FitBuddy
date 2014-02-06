package de.avalax.fitbuddy.app;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final int ADDITIONAL_FRAGMENTS = 1;
    protected WorkoutSession workoutSession;
    private String resultTitle;
    private String exerciseTitle;

    public MainPagerAdapter(FragmentManager fm, Context context, WorkoutSession workoutSession) {
        super(fm);
        this.workoutSession = workoutSession;
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.resultTitle = resources.getString(R.string.title_result);
        this.exerciseTitle = resources.getString(R.string.title_exercise);
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
            return resultTitle;
        }
        else {
            Exercise currentExercise = workout.getExercise(position);
            return String.format(exerciseTitle, currentExercise.getName(), currentExercise.getWeight());
        }
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
