package de.avalax.fitbuddy.presentation.workout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private WorkoutApplicationService workoutApplicationService;
    private String workoutId;

    public MainPagerAdapter(FragmentManager fm, WorkoutApplicationService workoutApplicationService, String workoutId) {
        super(fm);
        this.workoutApplicationService = workoutApplicationService;
        this.workoutId = workoutId;
    }

    @Override
    public Fragment getItem(int position) {
        return CurrentExerciseFragment.newInstance(workoutId, position);
    }

    @Override
    public int getCount() {
        return workoutApplicationService.countOfExercises(workoutId);
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}