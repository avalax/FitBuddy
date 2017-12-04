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
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutListFragment;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;
import de.avalax.fitbuddy.presentation.workout.ExerciseFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int EDIT_WORKOUT = 2;
    private Menu menu;
    private BottomNavigationView bottomNavigation;
    @Inject
    EditWorkoutApplicationService editWorkoutApplicationService;
    @Inject
    FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    @Inject
    WorkoutApplicationService workoutApplicationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((FitbuddyApplication) getApplication()).getComponent().inject(this);

        Fragment workoutListFragment = new WorkoutListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, workoutListFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        updateBottomNavigation();
    }

    public void updateBottomNavigation() {
        MenuItem itemWorkout = bottomNavigation.getMenu().getItem(1);
        MenuItem itemFinishedWorkout = bottomNavigation.getMenu().getItem(2);
        itemWorkout.setEnabled(workoutApplicationService.hasActiveWorkout());
        // TODO: performance
        boolean enabled = !finishedWorkoutApplicationService.loadAllFinishedWorkouts().isEmpty();
        itemFinishedWorkout.setEnabled(enabled);
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
            editWorkoutApplicationService.deleteWorkout(workout);
            removeWorkoutFromList(workout);
            mainToolbar();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            updateBottomNavigation();
        } catch (WorkoutException e) {
            Log.e("WorkoutException", e.getMessage(), e);
        }
        bottomNavigation.setSelectedItemId(R.id.navigation_workout_item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_workout_item) {
            Fragment exerciseFragment = new ExerciseFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, exerciseFragment)
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
        if (item.getItemId() == R.id.navigation_statistics_item) {
            Fragment finishedWorkoutListFragment = new FinishedWorkoutListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, finishedWorkoutListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigation.getSelectedItemId() == R.id.navigation_start_item) {
            super.onBackPressed();
        } else {
            bottomNavigation.setSelectedItemId(R.id.navigation_start_item);
        }
    }
}
