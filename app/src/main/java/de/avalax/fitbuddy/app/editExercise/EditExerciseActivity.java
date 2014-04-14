package de.avalax.fitbuddy.app.editExercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;


public class EditExerciseActivity extends Activity {

    private Exercise exercise;

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
        setContentView(R.layout.view_edit_exercise);
        init();
    }

    private void init() {
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        this.exercise = exercise;
    }
}