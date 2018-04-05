package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.ad_mod.AdMobProvider;
import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.application.billing.NotificationAsyncTask;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutService;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutService;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.presentation.dialog.SupportDialogFragment;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutListFragment;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;
import de.avalax.fitbuddy.presentation.workout.ExerciseFragment;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_WORKOUT;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_WORKOUT;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        SupportDialogFragment.DialogListener, NotificationAsyncTask.NotificationPostExecute {

    private Menu menu;
    private BottomNavigationView bottomNavigation;
    @Inject
    EditWorkoutService editWorkoutService;
    @Inject
    FinishedWorkoutService finishedWorkoutService;

    @Inject
    WorkoutService workoutService;

    @Inject
    BillingProvider billingProvider;

    @Inject
    AdMobProvider adMobProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-3067141613739864~9851773284");

        ((FitbuddyApplication) getApplication()).getComponent().inject(this);

        Fragment workoutListFragment = new WorkoutListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, workoutListFragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        mainToolbar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_edit_workout) {
            WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            workoutListFragment.removeSelection();
            mainToolbar();
            startActivityForResult(item.getIntent(), EDIT_WORKOUT);
            return true;
        }
        if (item.getItemId() == R.id.toolbar_delete_workout) {
            Workout workout = (Workout) item.getIntent().getSerializableExtra("workout");
            editWorkoutService.deleteWorkout(workout.getWorkoutId());
            removeWorkoutFromList(workout);
            mainToolbar();
            return true;
        }
        if (item.getItemId() == R.id.toolbar_delete_finished_workout) {
            removeSelectedFinishedWorkouts();
            mainToolbar();
            return true;
        }
        if (item.getItemId() == R.id.toolbar_support) {
            showSupportDialog();
            return true;
        }
        return false;
    }

    private void showSupportDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SupportDialogFragment.newInstance().show(fm, "fragment_support");
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

    private void addWorkoutToList(Workout workout) {
        WorkoutListFragment workoutListFragment = (WorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        workoutListFragment.addWorkout(workout);
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

    private void removeSelectedFinishedWorkouts() {
        FinishedWorkoutListFragment workoutListFragment = (FinishedWorkoutListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        workoutListFragment.removeFinishedWorkout();
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

    public void updateEditToolbar(int selectionCount) {
        if (selectionCount > 0) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_summary_edit, menu);
            MenuItem item = menu.findItem(R.id.toolbar_delete_finished_workout);
            item.setTitle(valueOf(selectionCount));
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
    }

    public void mainToolbar() {
        menu.clear();
        if (billingProvider.isPaid()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main_support, menu);
        }
        View adView = findViewById(R.id.adView);
        adMobProvider.initAdView(adView);
    }

    public void selectWorkout(Workout workout) {
        try {
            workoutService.switchWorkout(workout);
        } catch (WorkoutException e) {
            Log.e("WorkoutException", e.getMessage(), e);
        }
        bottomNavigation.setSelectedItemId(R.id.navigation_workout_item);
    }

    public void showSummary() {
        bottomNavigation.setSelectedItemId(R.id.navigation_summary_item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_workout_item) {
            boolean hasActiveWorkout = workoutService.hasActiveWorkout();
            if (hasActiveWorkout) {
                Fragment exerciseFragment = new ExerciseFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, exerciseFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit();
            } else {
                Context context = getApplicationContext();
                makeText(context, R.string.message_select_workout_first, LENGTH_SHORT)
                        .show();
            }
            return hasActiveWorkout;
        }
        if (item.getItemId() == R.id.navigation_start_item) {
            Fragment workoutListFragment = new WorkoutListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, workoutListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.navigation_summary_item) {
            Fragment finishedWorkoutListFragment = new FinishedWorkoutListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, finishedWorkoutListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
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

    @Override
    public void onDialogPositiveClick(SupportDialogFragment editRepsDialogFragment) {
        new NotificationAsyncTask(billingProvider, this).execute();
    }

    @Override
    public void onPostExecute(int result) {
        if (billingProvider.hasNotificationSend()) {
            makeText(getApplicationContext(), R.string.message_payment_available_soon, LENGTH_SHORT)
                    .show();
        }
    }
}
