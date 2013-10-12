package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class ExcerciseFragment extends RoboFragment{
	public static final String MAX_REPS = "max_reps";
	public static final String MAX_SETS = "max_sets";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exercise_object, container, false);
		Bundle args = getArguments();

		int maxReps = args.getInt(MAX_REPS);
		int maxSets = args.getInt(MAX_SETS);

		((FitBuddyProgressBar) rootView.findViewById(R.id.progressBarReps)).setMaxValue(maxReps);
		((FitBuddyProgressBar) rootView.findViewById(R.id.progressBarSets)).setMaxValue(maxSets);


		return rootView;
	}
}
