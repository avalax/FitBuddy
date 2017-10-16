package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;
import de.avalax.fitbuddy.presentation.workout.WorkoutFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_WORKOUT = 1;
    public static final int EDIT_WORKOUT = 2;
    @Inject
    protected WorkoutRepository workoutRepository;
    private Menu menu;
    private BottomNavigationView bottomNavigation;
    @Inject
    protected EditWorkoutApplicationService editWorkoutApplicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        ((FitbuddyApplication) getApplication()).getComponent().inject(this);

        Fragment workoutListFragment = new WorkoutListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, workoutListFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        workoutListFragment.updateWorkout(position, workout);
    }

    private void addWorkoutToList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        workoutListFragment.addWorkout(workout);
    }

    private void removeWorkoutFromList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_content);
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

    public void selectWorkout(Workout workout) {
        try {
            editWorkoutApplicationService.switchWorkout(workout);
        } catch (WorkoutException e) {
            Log.e("WorkoutException", e.getMessage(), e);
        }
        bottomNavigation.setSelectedItemId(R.id.navigation_workout_item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_workout_item) {
            Fragment workoutFragment = new WorkoutFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, workoutFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.navigation_start_item) {
            Fragment workoutListFragment = new WorkoutListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, workoutListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigation.getSelectedItemId() != R.id.navigation_start_item) {
            bottomNavigation.setSelectedItemId(R.id.navigation_start_item);
        } else {
            super.onBackPressed();
        }
    }
}
