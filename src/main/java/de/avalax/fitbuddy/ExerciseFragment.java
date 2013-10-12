package de.avalax.fitbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class ExerciseFragment extends RoboFragment{
	public static final String MAX_REPS = "max_reps";
	public static final String MAX_SETS = "max_sets";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exercise_object, container, false);
		Bundle args = getArguments();

		int maxReps = args.getInt(MAX_REPS);
		int maxSets = args.getInt(MAX_SETS);

        FitBuddyProgressBar fitBuddyProgressBarReps = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarReps);
        fitBuddyProgressBarReps.setMaxValue(maxReps);
        FitBuddyProgressBar fitBuddyProgressBarSets = (FitBuddyProgressBar) rootView.findViewById(R.id.progressBarSets);
        fitBuddyProgressBarSets.setMaxValue(maxSets);
        fitBuddyProgressBarSets.setCurrentValue(1);


		return rootView;
	}
}
