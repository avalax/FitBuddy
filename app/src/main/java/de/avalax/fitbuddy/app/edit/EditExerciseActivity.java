package de.avalax.fitbuddy.app.edit;

import android.app.Activity;
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
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.ManageWorkoutActivity;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;


public class EditExerciseActivity extends FragmentActivity {
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    private String newExerciseName;
    private EditableExercise editableExercise;
    @Inject
    protected WorkoutSession workoutSession;
    private int requestCode;
    private int exercisePosition;

    public static Intent newCreateExerciseIntent(Context context, int exercisePosition, int requestCode) {
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("exercisePosition", exercisePosition);
        return intent;
    }

    public static Intent newEditExerciseIntent(Context context, int exercisePosition) {
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra("requestCode", ManageWorkoutActivity.EDIT_EXERCISE);
        intent.putExtra("exercisePosition", exercisePosition);
        return intent;
    }

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
        this.requestCode = (int) getIntent().getSerializableExtra("requestCode");
        this.exercisePosition = (int) getIntent().getSerializableExtra("exercisePosition");
        this.editableExercise = getEditableExercise();
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
        int resultCode = RESULT_CANCELED;
        if (v.getId() == R.id.buttonSave) {
            if (editableExercise.getName() == null) {
                editableExercise.setName(newExerciseName);
            }
            resultCode = RESULT_OK;
            doEvent(resultCode);
        }
        if (v.getId() == R.id.buttonCancel) {
            resultCode = RESULT_FIRST_USER;
            doEvent(resultCode);
        }
        setResult(resultCode);
        finish();
    }

    private void doEvent(int resultCode) {
        Workout workout = workoutSession.getWorkout();
        if (resultCode == Activity.RESULT_OK) {
            Exercise exercise = editableExercise.createExercise();
            switch (requestCode) {
                case ManageWorkoutActivity.ADD_EXERCISE_BEFORE:
                    workout.addExerciseBefore(exercisePosition, exercise);
                    break;
                case ManageWorkoutActivity.ADD_EXERCISE_AFTER:
                    workout.addExerciseAfter(exercisePosition, exercise);
                    break;
                case ManageWorkoutActivity.EDIT_EXERCISE:
                    workout.setExercise(exercisePosition, exercise);
                    break;
            }
        } else if (resultCode == Activity.RESULT_FIRST_USER && requestCode == ManageWorkoutActivity.EDIT_EXERCISE) {
            workout.removeExercise(exercisePosition);
        }
    }

    private EditableExercise getEditableExercise() {
        if (requestCode == ManageWorkoutActivity.EDIT_EXERCISE) {
            return createExistingEditableExercise();
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

    private EditableExercise createExistingEditableExercise() {
        //TODO: factory pattern
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercise(exercisePosition);
        return new ExistingEditableExercise(exercise);
    }
}