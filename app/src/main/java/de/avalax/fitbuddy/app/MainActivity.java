package de.avalax.fitbuddy.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;
import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity {
    private static final int SWITCH_WORKOUT = 1;
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @Inject
    protected WorkoutSession workoutSession;
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
        inflater.inflate(R.menu.main_activity_actions, menu);
        this.menuItem = menu.findItem(R.id.action_title_weight);
        updatePage(this.position);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(null);

        this.position = 0;
        this.decimalFormat = new DecimalFormat("###.#");
        this.weightTitle = getResources().getString(R.string.title_weight);
        //TODO: 0 check, when workout has no exercise
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), workoutSession);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                updatePage(position);
            }
        });

        actionSwitchWorkout = getResources().getString(R.string.action_switch_workout);
    }

    private void updatePage(int position) {
        this.position = position;
        setTitle(workoutSession.getWorkout().getExercise(position).getName());
        if (menuItem != null) {
            menuItem.setTitle(exerciseWeightText(position));
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
            workoutSession.saveWorkout();
            Intent intent = new Intent(this, ManageWorkoutActivity.class);
            startActivityForResult(intent, SWITCH_WORKOUT);
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
}