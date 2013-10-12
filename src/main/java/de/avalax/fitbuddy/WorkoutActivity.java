package de.avalax.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.exercise)
public class WorkoutActivity extends RoboFragmentActivity {

    private static final int SET_TENDENCY_ON_RETURN = 1;
    private static final int DO_QUIT_ON_RESULT = 2;

    private ExercisePagerAdapter exercisePagerAdapter;
    private ViewPager viewPager;

    @Inject
    private Workout workout;

    private int exercisePosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercisePagerAdapter = new ExercisePagerAdapter(getSupportFragmentManager(), workout);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(exercisePagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, WorkoutActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startWorkoutResultActivity() {
        if (exercisePosition == workout.getExerciseCount()) {
            exercisePosition--;
        }
        startActivityForResult(new Intent(getApplicationContext(), WorkoutResultActivity.class), DO_QUIT_ON_RESULT);
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
                    //setViews(exercisePosition);
                } catch (ExerciseNotAvailableException e) {
                    startWorkoutResultActivity();
                }
            }
        }
    }
}

