package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.edit.EditExerciseActivity;
import de.avalax.fitbuddy.app.edit.EditableExercise;
import de.avalax.fitbuddy.app.edit.NewEditableExercise;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.view_pager)
public class WorkoutActivity extends RoboFragmentActivity implements UpdateableActivity {

    private static final int ADD_EXERCISE = 4;
    @Inject
    private Workout workout;
    @InjectView(R.id.pager)
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(new ExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.invalidate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_EXERCISE) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra("editableExercise");
            Exercise exercise = editableExercise.createExercise();
            workout.setExercise(0, exercise);
            notifyDataSetChanged();
        }
    }

    public void onClickEvent(View v) {
        if (v.getId() == R.id.buttonAddExercise) {
            startActivityForResult(getIntent(createNewEditableExercise()), ADD_EXERCISE);
        }
    }

    private NewEditableExercise createNewEditableExercise() {
        NewEditableExercise newEditableExercise = new NewEditableExercise();
        //TODO: extract to resources
        newEditableExercise.setWeight(2.5);
        newEditableExercise.setWeightRaise(1.25);
        newEditableExercise.setReps(12);
        newEditableExercise.setSets(3);
        return newEditableExercise;
    }

    private Intent getIntent(EditableExercise editableExercise) {
        Intent intent = new Intent(getApplicationContext(), EditExerciseActivity.class);
        intent.putExtra("editableExercise", editableExercise);
        return intent;
    }
}