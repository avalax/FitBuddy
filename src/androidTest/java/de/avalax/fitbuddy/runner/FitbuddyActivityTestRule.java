package de.avalax.fitbuddy.runner;

import android.support.test.rule.ActivityTestRule;

import javax.inject.Inject;

import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutService;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutService;
import de.avalax.fitbuddy.presentation.MainActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static de.avalax.fitbuddy.runner.TestFitbuddyApplication.TestComponent;

public class FitbuddyActivityTestRule extends ActivityTestRule<MainActivity> {
    @Inject
    EditWorkoutService editWorkoutService;
    @Inject
    WorkoutService workoutService;
    @Inject
    FinishedWorkoutService finishedWorkoutService;
    @Inject
    BillingProvider billingProvider;

    public FitbuddyActivityTestRule(Class<MainActivity> activityClass) {
        super(activityClass, false, false);
        TestFitbuddyApplication application =
                (TestFitbuddyApplication) getTargetContext().getApplicationContext();
        ((TestComponent) application.getComponent()).inject(this);
    }
}
