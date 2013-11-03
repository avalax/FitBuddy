package de.avalax.fitbuddy.edit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import roboguice.RoboGuice;

public class EditExercisePagerAdapter extends FragmentStatePagerAdapter {

    private EditableExercise editableExercise;

    public EditExercisePagerAdapter(FragmentManager fm, Context context, EditableExercise editableExercise) {
        super(fm);
        RoboGuice.getInjector(context).injectMembersWithoutViews(this);
        this.editableExercise = editableExercise;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new WeightExerciseFragment(editableExercise);
        } else if (position == 1) {
            return new EffortExerciseFragment(editableExercise);
        } else {
            return new ConfirmationExerciseFragment(editableExercise);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitleFor(position, editableExercise.getName());
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
