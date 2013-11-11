package de.avalax.fitbuddy.app.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.UpdateableActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.view_pager)
public class EditExerciseActivity extends RoboFragmentActivity implements UpdateableActivity{

    @InjectView(R.id.pager)
    private ViewPager viewPager;
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
        Intent returnIntent = new Intent();
        if (v.getId() == R.id.buttonSave) {
            if (editableExercise.getName() == null) {
                //TODO: extract to resources
                editableExercise.setName("new exercise");
            }
            returnIntent.putExtra("editableExercise", editableExercise);
            setResult(RESULT_OK, returnIntent);
        }
        if (v.getId() == R.id.buttonCancel) {
            setResult(RESULT_FIRST_USER);
        }
        finish();
    }

    private EditableExercise getEditableExercise() {
        return (EditableExercise) getIntent().getSerializableExtra("editableExercise");
    }

    private EditExercisePagerAdapter getEditExercisePagerAdapter() {
        return new EditExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext(), editableExercise);
    }

    @Override
    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
    }
}