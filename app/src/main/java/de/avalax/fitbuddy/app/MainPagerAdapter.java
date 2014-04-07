package de.avalax.fitbuddy.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.exceptions.ExerciseNotAvailableException;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    protected WorkoutSession workoutSession;

    public MainPagerAdapter(FragmentManager fm, WorkoutSession workoutSession) {
        super(fm);
        this.workoutSession = workoutSession;
    }

    @Override
    public Fragment getItem(int position) {
        return CurrentExerciseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        Workout workout = workoutSession.getWorkout();
        return workout.getExerciseCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Workout workout = workoutSession.getWorkout();
        try {
            Exercise currentExercise = workout.getExercise(position);
            return currentExercise.getName();
        } catch (ExerciseNotAvailableException e) {
            return "";
        }
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
