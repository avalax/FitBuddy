package de.avalax.fitbuddy.presentation.workout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.application.workout.WorkoutSession;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private WorkoutSession workoutSession;

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
        return workout != null ? workout.getExercises().size() : 0;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}