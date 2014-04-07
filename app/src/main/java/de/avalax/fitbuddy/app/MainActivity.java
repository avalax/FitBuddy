package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;
import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity implements PopupMenu.OnMenuItemClickListener {
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @InjectView(R.id.actionBarOverflow)
    protected ImageView actionBarOverflow;
    @InjectView(R.id.weightTextView)
    protected TextView weightTextView;
    @InjectView(R.id.indicator)
    protected PagerTitleStrip indicator;
    @Inject
    protected WorkoutSession workoutSession;
    protected String actionSwitchWorkout;
    private DecimalFormat decimalFormat;
    private String weightTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        getActionBar().hide();
        init();
    }

    private void init() {
        this.decimalFormat = new DecimalFormat("###.#");
        this.weightTitle = getResources().getString(R.string.title_weight);
        //TODO: 0 check, when workout has no exercise
        this.weightTextView.setText(exerciseWeightText(0));
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), workoutSession);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                weightTextView.setText(exerciseWeightText(position));
            }
        });
        weightTextView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (right != paddingRight(left, right)) {
                    indicator.setPadding(indicator.getPaddingLeft(), indicator.getPaddingTop(), paddingRight(left, right), indicator.getPaddingBottom());
                }
            }
        });

        actionSwitchWorkout = getResources().getString(R.string.action_switch_workout);
    }

    private int paddingRight(int left, int right) {
        final float scale = getResources().getDisplayMetrics().density;
        //TODO: extract 8dp to styles
        int paddingRight = (int) (8 * scale + 0.5f);
        return paddingRight + right - left;
    }

    private String exerciseWeightText(int position) {
        Exercise exercise = workoutSession.getWorkout().getExercise(position);
        double weight = exercise.getWeight();
        //TODO: show hide weight, change margins
        if (weight > 0) {
            return String.format(weightTitle, decimalFormat.format(weight));
        } else {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent upIntent = new Intent(this, MainActivity.class);
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


    @OnClick(R.id.actionBarOverflow)
    protected void switchWorkout() {
        PopupMenu popupMenu = new PopupMenu(this, actionBarOverflow);
        popupMenu.inflate(R.menu.main_activity_action);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (actionSwitchWorkout.equals(item.getTitle())) {
            workoutSession.saveWorkout();
            Intent intent = new Intent(this, ManageWorkoutActivity.class);
            startActivityForResult(intent, ManageWorkoutActivity.SWITCH_WORKOUT);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if ((requestCode == ManageWorkoutActivity.SAVE_WORKOUT || requestCode == ManageWorkoutActivity.SWITCH_WORKOUT) &&
                resultCode == Activity.RESULT_OK) {
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.invalidate();
            viewPager.setCurrentItem(0, true);
        }
    }
}