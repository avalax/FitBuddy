package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;
import de.avalax.fitbuddy.app.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;
import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity implements EditWeightDialogFragment.DialogListener {
    private static final int SWITCH_WORKOUT = 1;
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @Inject
    protected WorkoutSession workoutSession;
    @InjectView(R.id.workoutProgressBar)
    protected ProgressBar workoutProggressBar;
    protected String actionSwitchWorkout;
    private DecimalFormat decimalFormat;
    private String weightTitle;
    private MenuItem menuItem;
    private int position;

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
        updatePage(this.position);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(null);

        this.position = 0;
        this.decimalFormat = new DecimalFormat("###.###");
        this.weightTitle = getResources().getString(R.string.title_weight);
        //TODO: 0 check, when workout has no exercise
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), workoutSession));

        actionSwitchWorkout = getResources().getString(R.string.action_switch_workout);
    }

    @OnPageChange(R.id.pager)
    protected void updatePage(int position) {
        this.position = position;
        Workout workout = workoutSession.getWorkout();
        if (workout == null || workout.getExerciseCount() == 0) {
            switchWorkout();
        } else {
            setTitle(workout.getExercise(position).getName());
            if (menuItem != null) {
                menuItem.setTitle(exerciseWeightText(position));
                updateWorkoutProgress(position);
            }
        }
    }

    private String exerciseWeightText(int position) {
        Exercise exercise = workoutSession.getWorkout().getExercise(position);
        double weight = exercise.getWeight();
        if (weight > 0) {
            return String.format(weightTitle, decimalFormat.format(weight));
        } else {
            return "-";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch_workout) {
            switchWorkout();
        }
        if (item.getItemId() == R.id.action_change_weight) {
            showEditDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SWITCH_WORKOUT &&
                resultCode == Activity.RESULT_OK) {
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.invalidate();
            viewPager.setCurrentItem(0, true);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        double weight = workoutSession.getWorkout().getExercise(position).getWeight();
        EditWeightDialogFragment.newInstance(weight).show(fm, "fragment_edit_name");
    }

    protected void updateWorkoutProgress(int exercisePosition) {
        Workout workout = workoutSession.getWorkout();
        workoutProggressBar.setProgress(calculateProgressbarHeight(workout.getProgress(exercisePosition)));
    }

    private int calculateProgressbarHeight(double progess) {
        return (int) Math.round(progess * 100);
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        workoutSession.getWorkout().getExercise(position).getCurrentSet().setWeight(editWeightDialogFragment.getWeight());
        updatePage(position);
    }

    private void switchWorkout() {
        Intent intent = new Intent(this, ManageWorkoutActivity.class);
        startActivityForResult(intent, SWITCH_WORKOUT);
    }
}