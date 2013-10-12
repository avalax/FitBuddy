package de.avalax.fitbuddy;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;
import de.avalax.fitbuddy.workout.exceptions.RepsExceededException;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.workout)
public class WorkoutActivity extends RoboActivity implements View.OnClickListener {

    private static final int SET_TENDENCY_ON_RETURN = 1;
    private static final int DO_QUIT_ON_RESULT = 2;

    @Inject
    private Workout workout;

    private int exercisePosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.menu);
        getActionBar().setBackgroundDrawable(null);
        FitBuddyProgressBar repsProgressBar = (FitBuddyProgressBar) findViewById(R.id.progressBarReps);
        repsProgressBar.setOnClickListener(this);
        FitBuddyProgressBar setsProgressBar = (FitBuddyProgressBar) findViewById(R.id.progressBarSets);
        setsProgressBar.setOnClickListener(this);
        setViews(exercisePosition);

    }

    private void setViews(int exercisePosition) {
        //TODO: IOC
        ((TextView) findViewById(R.id.textViewExercise)).setText(workout.getName(exercisePosition));
        ((FitBuddyProgressBar) findViewById(R.id.progressBarReps)).setProgressBar(workout.getExercise(exercisePosition).getCurrentSet());
        ((FitBuddyProgressBar) findViewById(R.id.progressBarSets)).setProgressBar(workout.getExercise(exercisePosition));
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.imageButtonNext) {
            try {
                exercisePosition++;
                setViews(exercisePosition);
            } catch (ExerciseNotAvailableException e) {
                startWorkoutResultActivity();
            }
        }
        if (v.getId() == R.id.imageButtonPrevious) {
            try {
                exercisePosition--;
                setViews(exercisePosition);
            } catch (ExerciseNotAvailableException e) {}
        }
    }

    private void startWorkoutResultActivity() {
        if (exercisePosition == workout.getExerciseCount()) {
            exercisePosition--;
        }
        startActivityForResult(new Intent(getApplicationContext(), WorkoutResultActivity.class), DO_QUIT_ON_RESULT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.progressBarReps) {
            Set currentSet = workout.getExercise(exercisePosition).getCurrentSet();
            try {
                currentSet.setReps(currentSet.getReps() + 1);
            } catch (RepsExceededException ree) {
                incrementSetNumber();
            }
            ((FitBuddyProgressBar) v).setProgressBar(currentSet);
        }
        if (v.getId() == R.id.progressBarSets) {
            incrementSetNumber();
        }
        try {
            setViews(exercisePosition);
        } catch (ExerciseNotAvailableException e) {
            startWorkoutResultActivity();
        }
    }

    private void incrementSetNumber() {
        try {
            workout.incrementSet(exercisePosition);
        } catch (SetNotAvailableException snae) {
            if (workout.getExercise(exercisePosition).getTendency() == null) {
                startTendencyActivity();
            } else {
                exercisePosition++;
            }
        }
    }

    private void startTendencyActivity() {
        Intent intent = new Intent(getApplicationContext(), TendencyActivity.class);
        intent.putExtra("exercise", workout.getExercise(exercisePosition));
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
                    workout.setTendency(exercisePosition, tendency);
                    exercisePosition++;
                    setViews(exercisePosition);
                } catch (ExerciseNotAvailableException e) {
                    startWorkoutResultActivity();
                }
            }
        }
    }
}

