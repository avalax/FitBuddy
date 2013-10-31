package de.avalax.fitbuddy.edit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.workout.Exercise;
import roboguice.RoboGuice;

public class EditExercisePagerAdapter extends FragmentStatePagerAdapter {

    private Exercise exercise;

    public EditExercisePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        RoboGuice.getInjector(context).injectMembersWithoutViews(this);
    }

    public EditExercisePagerAdapter(FragmentManager fm, Context context, Exercise exercise) {
        this(fm, context);
        this.exercise = exercise;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EditWeightExerciseFragment();
        } else if (position == 1) {
            return new EditEffortExerciseFragment();
        } else {
            return new EditConfirmationExerciseFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //TODO: resources
        if (exercise == null) {
            return getTitleFor(position, "new exercise");
        }
        return getTitleFor(position, exercise.getName());
    }

    private CharSequence getTitleFor(int position, String name) {
        if (position == 0) {
            return String.format("weight - %1s", name);
        } else if (position == 1) {
            return String.format("effort - %1s", name);
        } else {
            return String.format("save - %1s", name);
        }
    }
}
