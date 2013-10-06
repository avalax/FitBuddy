package de.avalax.fitbuddy;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.basic.BasicExercise;
import de.avalax.fitbuddy.workout.basic.BasicSet;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;
import de.avalax.fitbuddy.workout.exceptions.RepetitionsExceededException;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.workout)
public class WorkoutActivity extends RoboActivity implements View.OnClickListener{

    private static final int SET_TENDENCY_ON_RETURN = 1;
    private static final int DO_QUIT_ON_RESULT = 2;
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
        repsProgressBar.setOnClickListener(this);
        FitBuddyProgressBar setsProgressBar = (FitBuddyProgressBar) findViewById(R.id.progressBarSets);
        setsProgressBar.setOnClickListener(this);
        setViews();

    }

    private void setViews() {
        //TODO: Change to Workout getName()
        //TODO: IOC
        ((TextView) findViewById(R.id.textViewExercise)).setText(workout.getCurrentExercise().getName());
        ((FitBuddyProgressBar) findViewById(R.id.progressBarRepetitions)).setProgressBar(workout.getCurrentExercise().getCurrentSet());
        ((FitBuddyProgressBar) findViewById(R.id.progressBarSets)).setProgressBar(workout.getCurrentExercise());
    }

    private Workout createTestWorkout() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new BasicExercise("Bankdrücken", createSetWithThreeSets(70), 5));
        exercises.add(new BasicExercise("Situps", createSetWithThreeSets(1.5), 2.5));
        return new BasicWorkout(exercises);
    }

    private List<Set> createSetWithThreeSets(double weight) {
        List<Set> sets = new ArrayList<Set>();
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        return sets;
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.imageButtonNext) {
            try {
                workout.switchToNextExercise();
                setViews();
            } catch (ExerciseNotAvailableException e) {
                startActivityForResult(new Intent(getApplicationContext(), WorkoutResultActivity.class), DO_QUIT_ON_RESULT);
            }
        }
        if (v.getId() == R.id.imageButtonPrevious) {
            try {
                workout.switchToPreviousExercise();
                setViews();
            } catch (ExerciseNotAvailableException e) {
                //TODO: Show next activity
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.progressBarRepetitions) {
            Set currentSet = workout.getCurrentExercise().getCurrentSet();
            try {
                currentSet.setRepetitions(currentSet.getRepetitions()+1); //TODO: make method incrementRepetitions
            } catch (RepetitionsExceededException ree) {
                incrementSetNumber();
            }
            ((FitBuddyProgressBar) v).setProgressBar(currentSet);
        }
        if (v.getId() == R.id.progressBarSets) {
            incrementSetNumber();
        }
        setViews();
    }

    private void incrementSetNumber() {
        try {
            workout.getCurrentExercise().incrementSetNumber(); //TODO:workout.incrementSetNumber
        } catch (SetNotAvailableException snae) {
            if (workout.getCurrentExercise().getTendency() == null) {
                startTendencyActivity();
            } else {
                try {
                    workout.switchToNextExercise();
                } catch (ExerciseNotAvailableException e) {
                    startActivityForResult(new Intent(getApplicationContext(), WorkoutResultActivity.class), DO_QUIT_ON_RESULT);
                }
            }
        }
    }

    private void startTendencyActivity() {
        Intent intent = new Intent(getApplicationContext(), TendencyActivity.class);
        intent.putExtra("exercise", workout.getCurrentExercise());
        startActivityForResult(intent, SET_TENDENCY_ON_RETURN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode > 0) {
            if (requestCode == DO_QUIT_ON_RESULT) {
                //TODO: Zeige StartScreen
            }

            if (requestCode == SET_TENDENCY_ON_RETURN) {
                Tendency tendency = (Tendency) data.getSerializableExtra("tendency");
                try {
                    workout.setTendency(tendency);
                    //TODO: set weight based on tendency and weight step
                    setViews();
                } catch (ExerciseNotAvailableException e) {
                    startActivityForResult(new Intent(getApplicationContext(), WorkoutResultActivity.class), DO_QUIT_ON_RESULT);
                }
            }
        }
    }
}

