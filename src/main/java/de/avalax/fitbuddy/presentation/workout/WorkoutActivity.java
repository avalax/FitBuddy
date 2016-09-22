package de.avalax.fitbuddy.presentation.workout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment.DialogListener;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutActivity;

public class WorkoutActivity extends FragmentActivity implements DialogListener {
    private static final int MANAGE_WORKOUT = 1;
    private ViewPager viewPager;
    private ProgressBar workoutProgressBar;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    private MenuItem menuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ((FitbuddyApplication) getApplication()).inject(this);
        init();
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(null);

        viewPager = (ViewPager) findViewById(R.id.pager);
        workoutProgressBar = (ProgressBar) findViewById(R.id.workoutProgressBar);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updatePage(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_actions, menu);
        this.menuItem = menu.findItem(R.id.action_change_weight);
        try {
            initWorkoutActivity();
        } catch (ResourceException e) {
            Log.d("workout not found", e.getMessage(), e);
            startManageWorkoutActivity();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initWorkoutActivity() throws ResourceException {
        int countOfExercises = workoutApplicationService.countOfExercises();
        viewPager.setAdapter(new ExercisePager(getSupportFragmentManager(), countOfExercises));
        viewPager.setCurrentItem(workoutApplicationService.indexOfCurrentExercise());
        updatePage(workoutApplicationService.indexOfCurrentExercise());
    }

    protected void updatePage(int index) {
        try {
            workoutApplicationService.setCurrentExercise(index);
            Exercise exercise = workoutApplicationService.requestExercise(index);
            setTitle(exerciseViewHelper.nameOfExercise(exercise));
            if (menuItem != null) {
                menuItem.setTitle(exerciseViewHelper.weightOfExercise(exercise));
                updateWorkoutProgress(index);
            }
        } catch (ResourceException e) {
            Log.d("can't update page", e.getMessage(), e);
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
        if (item.getItemId() == R.id.action_finish_workout) {
            finishActiveWorkout();
        }
        if (item.getItemId() == R.id.action_display_finished_workouts) {
            displayFinishedWorkouts();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MANAGE_WORKOUT && resultCode == Activity.RESULT_OK) {
            try {
                initWorkoutActivity();
            } catch (ResourceException e) {
                Log.d("workout not found", e.getMessage(), e);
            }
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        try {
            int index = workoutApplicationService.indexOfCurrentExercise();
            double weight = workoutApplicationService.weightOfCurrentSet(index);
            EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_name");
        } catch (ResourceException e) {
            Log.d("Can't edit weight", e.getMessage(), e);
        }
    }

    private void displayFinishedWorkouts() {
        Intent intent = new Intent(this, FinishedWorkoutActivity.class);
        startActivity(intent);
    }

    protected void updateWorkoutProgress(int exerciseIndex) {
        try {
            int workoutProgress = workoutApplicationService.workoutProgress(exerciseIndex);
            workoutProgressBar.setProgress(workoutProgress);
        } catch (ResourceException e) {
            Log.d("Can't change progress", e.getMessage(), e);
        }
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        try {
            int index = workoutApplicationService.indexOfCurrentExercise();
            double weight = editWeightDialogFragment.getWeight();
            workoutApplicationService.updateWeightOfCurrentSet(index, weight);
            updatePage(index);
        } catch (ResourceException e) {
            Log.d("Can't edit weight", e.getMessage(), e);
        }
    }

    private void startManageWorkoutActivity() {
        Intent intent = new Intent(this, EditWorkoutActivity.class);
        startActivityForResult(intent, MANAGE_WORKOUT);
    }

    private void finishActiveWorkout() {
        try {
            workoutApplicationService.finishCurrentWorkout();
            initWorkoutActivity();
        } catch (ResourceException e) {
            Log.d("Can not finish workout", e.getMessage(), e);
        }
    }
}