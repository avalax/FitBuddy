package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_WORKOUT = 1;
    public static final int EDIT_WORKOUT = 2;
    @Inject
    protected WorkoutRepository workoutRepository;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ((FitbuddyApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddWorkoutButtonClick(View view) {
        Intent intent = new Intent(this, EditWorkoutActivity.class);
        intent.putExtra("workout", new BasicWorkout());
        startActivityForResult(intent, ADD_WORKOUT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_edit_workout) {
            startActivityForResult(item.getIntent(), EDIT_WORKOUT);
            return true;
        }
        if (item.getItemId() == R.id.toolbar_delete_workout) {
            Workout workout = (Workout) item.getIntent().getSerializableExtra("workout");
            workoutRepository.delete(workout.getWorkoutId());
            removeWorkoutFromList(workout);
            mainToolbar();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_WORKOUT && resultCode == Activity.RESULT_OK) {
            Workout workout = (Workout) data.getSerializableExtra("workout");
            addWorkoutToList(workout);
        }
        if (requestCode == EDIT_WORKOUT && resultCode == Activity.RESULT_OK) {
            Workout workout = (Workout) data.getSerializableExtra("workout");
            Integer position = data.getIntExtra("position", -1);
            updateWorkoutInList(position, workout);
        }
    }

    private void updateWorkoutInList(Integer position, Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
        workoutListFragment.updateWorkout(position, workout);
    }

    private void addWorkoutToList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
        workoutListFragment.addWorkout(workout);
    }

    private void removeWorkoutFromList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
        workoutListFragment.removeWorkout(workout);
    }

    public void updateEditToolbar(int position, Workout workout) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main_edit_workout, menu);
        Intent intent = new Intent(this, EditWorkoutActivity.class);
        intent.putExtra("workout", workout);
        intent.putExtra("position", position);
        MenuItem item = menu.findItem(R.id.toolbar_edit_workout);
        item.setIntent(intent);
        MenuItem itemDelete = menu.findItem(R.id.toolbar_delete_workout);
        itemDelete.setIntent(intent);
    }

    private void mainToolbar() {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
    }
}
