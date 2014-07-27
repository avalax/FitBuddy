package de.avalax.fitbuddy.presentation.workout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.edit.workout.ManageWorkoutActivity;

import javax.inject.Inject;
import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity implements EditWeightDialogFragment.DialogListener {
    private static final int MANAGE_WORKOUT = 1;
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @Inject
    protected WorkoutApplicationService workoutApplicationService;
    @Inject
    protected SharedPreferences sharedPreferences;
    @InjectView(R.id.workoutProgressBar)
    protected ProgressBar workoutProggressBar;
    protected String actionSwitchWorkout;
    private DecimalFormat decimalFormat;
    private String weightTitle;
    private MenuItem menuItem;
    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        this.menuItem = menu.findItem(R.id.action_change_weight);
        updatePage(this.index);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(null);

        this.index = 0;
        this.decimalFormat = new DecimalFormat("###.###");
        this.weightTitle = getResources().getString(R.string.title_weight);
        String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
        viewPager.setOffscreenPageLimit(workoutApplicationService.countOfExercises(workoutId));
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), workoutApplicationService, workoutId));

        actionSwitchWorkout = getResources().getString(R.string.action_switch_workout);
    }

    @OnPageChange(R.id.pager)
    protected void updatePage(int index) {
        this.index = index;
        String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
        try {
            Exercise exercise = workoutApplicationService.exerciseFromPosition(workoutId, index);
            //TODO: 3x times - unnamed exercise from resources & move to a ui helper
            setTitle(exercise.getName().length() > 0 ? exercise.getName() : "unnamed exercise");
            if (menuItem != null) {
                menuItem.setTitle(exerciseWeightText(exercise));
                updateWorkoutProgress(index);
            }
        } catch (ExerciseNotFoundException e) {
            Log.d("exercise not found at position", e.getMessage(), e);
            startManageWorkoutActivity();
        }
    }

    private String exerciseWeightText(Exercise exercise) {
        //TODO: helper method
        if (exercise.getSets().isEmpty()) {
            return "-";
        }
        double weight = exercise.getCurrentSet().getWeight();
        if (weight > 0) {
            return String.format(weightTitle, decimalFormat.format(weight));
        } else {
            return "-";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch_workout) {
            startManageWorkoutActivity();
        }
        if (item.getItemId() == R.id.action_change_weight) {
            showEditDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MANAGE_WORKOUT &&
                resultCode == Activity.RESULT_OK) {
            String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
            viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), workoutApplicationService, workoutId));
            updatePage(0);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
        try {
            Exercise exercise = workoutApplicationService.exerciseFromPosition(workoutId, index);
            double weight = exercise.getCurrentSet().getWeight();
            EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_name");
        } catch (ExerciseNotFoundException e) {
            Log.d("Can edit weight of current set", e.getMessage(), e);
        }
    }

    protected void updateWorkoutProgress(int exerciseIndex) {
        String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
        try {
            Workout workout = workoutApplicationService.requestWorkout(workoutId);
            workoutProggressBar.setProgress(calculateProgressbarHeight(workout.getProgress(exerciseIndex)));
        } catch (WorkoutNotFoundException e) {
            Log.d("updateWorkoutProgress failed", e.getMessage(), e);
        }
    }

    private int calculateProgressbarHeight(double progess) {
        return (int) Math.round(progess * 100);
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        String workoutId = sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "1");
        try {
            Exercise exercise = workoutApplicationService.exerciseFromPosition(workoutId, index);
            exercise.getCurrentSet().setWeight(editWeightDialogFragment.getWeight());
            updatePage(index);
        } catch (ExerciseNotFoundException e) {
            Log.d("Can edit weight of current set", e.getMessage(), e);
        }
    }

    private void startManageWorkoutActivity() {
        Intent intent = new Intent(this, ManageWorkoutActivity.class);
        startActivityForResult(intent, MANAGE_WORKOUT);
    }
}