package de.avalax.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.google.inject.Inject;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.exercise)
public class WorkoutActivity extends RoboFragmentActivity {

    @InjectView(R.id.pager)
    private ViewPager viewPager;

    @Inject
    private Workout workout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(new ExercisePagerAdapter(getSupportFragmentManager(),getApplicationContext(), workout));
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
}

