package de.avalax.fitbuddy.runner;

import android.support.test.rule.ActivityTestRule;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.MainActivity;

import static de.avalax.fitbuddy.runner.TestFitbuddyApplication.TestComponent;

public class FitbuddyActivityTestRule extends ActivityTestRule<MainActivity> {
    @Inject
    WorkoutRepository workoutRepository;

    public FitbuddyActivityTestRule(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        TestFitbuddyApplication application = (TestFitbuddyApplication) getActivity().getApplication();
        ((TestComponent) application.getComponent()).inject(this);
    }

    public void deleteWorkouts() {
        List<WorkoutListEntry> workoutList = workoutRepository.getWorkoutList();
        for (WorkoutListEntry workoutListEntry : workoutList) {
            workoutRepository.delete(workoutListEntry.getWorkoutId());
        }
    }
}
