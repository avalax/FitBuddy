package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Context;
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

@ContentView(R.layout.activity_view_pager)
public class MainActivity extends RoboFragmentActivity implements UpdateableActivity {

    private static final int ADD_EXERCISE = 4;
    @Inject
    Context context;
    @Inject
    private Workout workout;
    @InjectView(R.id.pager)
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
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

    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.invalidate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_EXERCISE) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE);
            Exercise exercise = editableExercise.createExercise();
            workout.setExercise(0, exercise);
            notifyDataSetChanged();
        }
    }

    public void onClickEvent(View v) {
        if (v.getId() == R.id.buttonAddExercise) {
            startEditExerciseActivity(context, createNewEditableExercise(), ADD_EXERCISE);
        }
    }

    private void startEditExerciseActivity(Context context, EditableExercise editableExercise, int requestCode) {
        //TODO: merge with ExerciseFragment - startEditExerciseActivity
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE, editableExercise);
        startActivityForResult(intent, requestCode);
    }

    private NewEditableExercise createNewEditableExercise() {
        NewEditableExercise newEditableExercise = new NewEditableExercise();
        //TODO: extract to resources / factory pattern
        newEditableExercise.setWeight(2.5);
        newEditableExercise.setWeightRaise(1.25);
        newEditableExercise.setReps(12);
        newEditableExercise.setSets(3);
        return newEditableExercise;
    }
}