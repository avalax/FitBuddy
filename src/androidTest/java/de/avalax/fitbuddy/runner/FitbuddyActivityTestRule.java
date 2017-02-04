package de.avalax.fitbuddy.runner;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import javax.inject.Inject;

import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.presentation.MainActivity;

import static de.avalax.fitbuddy.runner.TestFitbuddyApplication.TestComponent;

public class FitbuddyActivityTestRule extends ActivityTestRule<MainActivity> {

    @Inject
    WorkoutSession workoutSession;

    public FitbuddyActivityTestRule(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        TestFitbuddyApplication application = (TestFitbuddyApplication) getActivity().getApplication();
        ((TestComponent)application.getComponent()).inject(this);
    }

    public WorkoutSession getWorkoutSession() {
        return workoutSession;
    }

    public MainActivity launchActivity() {
        return super.launchActivity(new Intent());
    }
}
