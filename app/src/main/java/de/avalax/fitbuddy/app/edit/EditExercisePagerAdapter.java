package de.avalax.fitbuddy.app.edit;

import android.content.Context;
import android.os.Bundle;
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
        Fragment fragment;
        Bundle args = new Bundle();
        args.putSerializable("editableExercise", editableExercise);
        if (position == 0) {
            fragment = new WeightExerciseFragment();
        } else if (position == 1) {
            fragment = new EffortExerciseFragment();
        } else {
            fragment = new ConfirmationExerciseFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = editableExercise.getName();
        if (title == null) {
            //TODO: extract to resources
            title = "new exercise";
        }
        return getTitleFor(position, title);
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
