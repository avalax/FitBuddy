package de.avalax.fitbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;

public class ExcercisePagerAdapter extends FragmentStatePagerAdapter{
	private Workout workout;

	public ExcercisePagerAdapter(FragmentManager fm , Workout workout){
		super(fm);
		this.workout = workout;
	}

	@Override
	public Fragment getItem(int exerciseIndex){
		Fragment fragment = new ExcerciseFragment();
		Bundle args = new Bundle();

		Exercise currentExercise = workout.getExercise(exerciseIndex);
		args.putInt(ExcerciseFragment.MAX_REPS, currentExercise.getCurrentSet().getMaxReps());//TODO:CurrentExersice getMaxReps
		args.putInt(ExcerciseFragment.MAX_SETS, currentExercise.getSetNumber());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return workout.getExerciseCount();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Exercise currentExercise = workout.getExercise(position);

		return currentExercise.getName();
	}
}
