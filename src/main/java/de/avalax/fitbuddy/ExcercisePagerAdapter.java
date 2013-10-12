package de.avalax.fitbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sun.xml.internal.bind.v2.TODO;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;

public class ExcercisePagerAdapter extends FragmentStatePagerAdapter{
	private BasicWorkout workout;

	public ExcercisePagerAdapter(FragmentManager fm , BasicWorkout workout){
		super(fm);
		this.workout = workout;
	}

	@Override
	public Fragment getItem(int exerciseIndex){
		Fragment fragment = new ExcerciseFragment();
		Bundle args = new Bundle();

		Exercise currentExercise = workout.getExercise(exerciseIndex);
		args.putInt(ExcerciseFragment.MAX_REPS, currentExercise.getReps());
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
