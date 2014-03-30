package de.avalax.fitbuddy.app.editExercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;


public class EditExerciseActivity extends FragmentActivity {
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    private String newExerciseName;
    private EditableExercise editableExercise;

    public static Intent newCreateExerciseIntent(Context context) {
        return new Intent(context, EditExerciseActivity.class);
    }

    public static Intent newEditExerciseIntent(Context context, Exercise exercise) {
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra("exercise", exercise);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.inject(this);
        getActionBar().hide();
        init();
    }

    private void init() {
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        this.editableExercise = getEditableExercise(exercise);
        this.newExerciseName = getResources().getString(R.string.new_exercise_name);
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
            Intent intent = new Intent();
            intent.putExtra("editableExercise",editableExercise);
            setResult(RESULT_OK, intent);
        }
        if (v.getId() == R.id.buttonCancel) {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private EditableExercise getEditableExercise(Exercise exercise) {
        if (exercise != null) {
            return createExistingEditableExercise(exercise);
        }
        else {
            return createNewEditableExercise();
        }
    }

    private EditExercisePagerAdapter getEditExercisePagerAdapter() {
        return new EditExercisePagerAdapter(getSupportFragmentManager(), getApplicationContext(), editableExercise);
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

    private EditableExercise createExistingEditableExercise(Exercise exercise) {
        return new ExistingEditableExercise(exercise);
    }
}