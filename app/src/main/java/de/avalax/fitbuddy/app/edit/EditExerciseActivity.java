package de.avalax.fitbuddy.app.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import de.avalax.fitbuddy.app.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_view_pager)
public class EditExerciseActivity extends RoboFragmentActivity {
    public static final String EXTRA_EDITABLE_EXERCISE = "editableExercise";
    @InjectView(R.id.pager)
    private ViewPager viewPager;
    @InjectResource(R.string.new_exercise_name)
    private String newExerciseName;
    private EditableExercise editableExercise;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.editableExercise = getEditableExercise();
        viewPager.setAdapter(getEditExercisePagerAdapter());
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

    public void clickEvent(View v) {
        if (v.getId() == R.id.buttonSave) {
            if (editableExercise.getName() == null) {
                editableExercise.setName(newExerciseName);
            }
            setResult(RESULT_OK, createIntentResult());
        }
        if (v.getId() == R.id.buttonCancel) {
            setResult(RESULT_FIRST_USER);
        }
        finish();
    }

    private Intent createIntentResult() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_EDITABLE_EXERCISE, editableExercise);
        return returnIntent;
    }

    private EditableExercise getEditableExercise() {
        return (EditableExercise) getIntent().getSerializableExtra(EXTRA_EDITABLE_EXERCISE);
    }

    private EditExercisePagerAdapter getEditExercisePagerAdapter() {
        return new EditExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext(), editableExercise);
    }
}