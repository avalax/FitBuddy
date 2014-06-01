package de.avalax.fitbuddy.app.manageWorkout.editExercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import butterknife.ButterKnife;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

public class EditExerciseActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workout);
        ButterKnife.inject(this);
        ((FitbuddyApplication) getApplication()).inject(this);
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        Fragment fragment = EditExerciseDialogFragment.newInstance(exercise);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
        getActionBar().setTitle(exercise.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_exercise_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}