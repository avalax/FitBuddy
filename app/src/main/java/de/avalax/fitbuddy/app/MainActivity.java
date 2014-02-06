package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements PopupMenu.OnMenuItemClickListener {
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @InjectView(R.id.actionBarOverflow)
    protected ImageView actionBarOverflow;
    @Inject
    protected WorkoutSession workoutSession;
    protected String actionSwitchWorkout;

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
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(), workoutSession));
        actionSwitchWorkout = getResources().getString(R.string.action_switch_workout);
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