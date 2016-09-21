package de.avalax.fitbuddy.presentation.workout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ExercisePager extends FragmentStatePagerAdapter {

    private final int count;

    public ExercisePager(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        return ExerciseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}