package de.avalax.fitbuddy;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.WorkoutSet;
import de.avalax.fitbuddy.workout.basic.BasicSet;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.workout.basic.BasicWorkoutSet;
import de.avalax.fitbuddy.workout.exceptions.WorkoutSetNotAvailableException;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class WorkoutActivity extends RoboActivity {

    private Workout workout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.menu);
        getActionBar().setBackgroundDrawable(null);
        workout = createTestWorkout();
	      FitBuddyProgressBar repsProgressBar = (FitBuddyProgressBar) findViewById(R.id.progressBarRepetitions);
	      repsProgressBar.setProgressBar(workout.getCurrentWorkoutSet().getCurrentSet());
	      FitBuddyProgressBar setsProgressBar = (FitBuddyProgressBar) findViewById(R.id.progressBartSets);
	      setsProgressBar.setProgressBar(workout.getCurrentWorkoutSet());

        setViews();

    }

    private void setViews() {
        //TODO: Change to Workout getName()
        ((TextView)findViewById(R.id.textViewWorkoutSet)).setText(workout.getCurrentWorkoutSet().getName());
    }

    private Workout createTestWorkout() {
        List<WorkoutSet> workoutSets = new ArrayList<WorkoutSet>();
        workoutSets.add(new BasicWorkoutSet("Bankdrücken", createSetWithThreeSets(70)));
        workoutSets.add(new BasicWorkoutSet("Situps", createSetWithThreeSets(0)));
        return new BasicWorkout(workoutSets);
    }

    private List<Set> createSetWithThreeSets(int weight) {
        List<Set> sets = new ArrayList<Set>();
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        return sets;
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.imageButtonNext) {
            try {
                workout.incrementWorkoutSetNumber();
                setViews();
            } catch (WorkoutSetNotAvailableException e) {
                //TODO: Show next activity
            }
        }
        if (v.getId() == R.id.imageButtonPrevious) {
            try {
                workout.decrementWorkoutSetNumber();
                setViews();
            } catch (WorkoutSetNotAvailableException e) {
                //TODO: Show next activity
            }
        }
    }
}

