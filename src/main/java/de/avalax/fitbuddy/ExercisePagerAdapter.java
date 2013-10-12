package de.avalax.fitbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;

public class ExercisePagerAdapter extends FragmentStatePagerAdapter {
    private Workout workout;

    public ExercisePagerAdapter(FragmentManager fm, Workout workout) {
        super(fm);
        this.workout = workout;
    }

    @Override
    public Fragment getItem(int exerciseIndex) {
        if (exerciseIndex < workout.getExerciseCount()) {
            Fragment fragment = new ExerciseFragment();
            Bundle args = new Bundle();
            args.putInt("exerciseIndex", exerciseIndex);
            fragment.setArguments(args);

            return fragment;
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

            return currentExercise.getName();
        } else {
            return "Ergebnis"; //TODO: move to strings.xml
        }
    }
}
