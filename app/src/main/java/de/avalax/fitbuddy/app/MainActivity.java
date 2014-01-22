package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.edit.EditExerciseActivity;
import de.avalax.fitbuddy.app.edit.EditableExercise;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity {
    private static final int ADD_EXERCISE = 4;
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @Inject
    protected WorkoutSession workoutSession;

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
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(),workoutSession));
        viewPager.setCurrentItem(1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_EXERCISE) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE);
            Exercise exercise = editableExercise.createExercise();
            Workout workout = workoutSession.getWorkout();
            workout.setExercise(0, exercise);
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.invalidate();
        }
    }
}