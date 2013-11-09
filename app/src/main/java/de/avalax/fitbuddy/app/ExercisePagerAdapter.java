package de.avalax.fitbuddy.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.inject.Inject;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;

public class ExercisePagerAdapter extends FragmentStatePagerAdapter {
    @Inject
    private Workout workout;
    @InjectResource(R.string.result_title)
    private String resultTitle;
    @InjectResource(R.string.welcome_title)
    private String welcomeTitle;

    public ExercisePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        RoboGuice.getInjector(context).injectMembersWithoutViews(this);
    }

    @Override
    public Fragment getItem(int exerciseIndex) {
        if (exerciseIndex < workout.getExerciseCount()) {
            Fragment fragment = new ExerciseFragment();
            Bundle args = new Bundle();
            args.putInt("exerciseIndex", exerciseIndex);
            fragment.setArguments(args);
            return fragment;
        } else if (workout.getExerciseCount() == 0) {
            return new FirstStartFragment();
        } else {
            return new WorkoutResultFragment();
        }
    }

    @Override
    public int getCount() {
        return workout.getExerciseCount() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < workout.getExerciseCount()) {
            Exercise currentExercise = workout.getExercise(position);

            return String.format("%1$s : %2$skg", currentExercise.getName(), currentExercise.getWeight());
        } else if (workout.getExerciseCount() == 0) {
            return welcomeTitle;
        } else {
            return resultTitle;
        }
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
