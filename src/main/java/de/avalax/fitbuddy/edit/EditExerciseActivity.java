package de.avalax.fitbuddy.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.workout.Exercise;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.view_pager)
public class EditExerciseActivity extends RoboFragmentActivity {
    @InjectView(R.id.pager)
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        EditExercisePagerAdapter editExercisePagerAdapter = getEditExercisePagerAdapter(exercise);
        viewPager.setAdapter(editExercisePagerAdapter);
    }

    private EditExercisePagerAdapter getEditExercisePagerAdapter(Exercise exercise) {
        EditExercisePagerAdapter editExercisePagerAdapter;
        if (exercise != null) {
            editExercisePagerAdapter = new EditExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext(), exercise);
        } else {
            editExercisePagerAdapter = new EditExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext());
        }
        return editExercisePagerAdapter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent upIntent = new Intent(this, EditExerciseActivity.class);
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