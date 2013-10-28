package de.avalax.fitbuddy.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.inject.Inject;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.edit.EditConfirmationExerciseFragment;
import de.avalax.fitbuddy.edit.EditEffortExerciseFragment;
import de.avalax.fitbuddy.edit.EditWeightExerciseFragment;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;

public class EditExercisePagerAdapter extends FragmentStatePagerAdapter {

    public EditExercisePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        RoboGuice.getInjector(context).injectMembersWithoutViews(this);
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
        if (position == 0) {
            return "edit";
        } else if (position == 1) {
            return "edit2";
        } else {
            return "edit3";
        }
    }
}
