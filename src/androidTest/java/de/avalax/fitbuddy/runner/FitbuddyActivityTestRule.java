package de.avalax.fitbuddy.runner;

import android.support.test.rule.ActivityTestRule;

import javax.inject.Inject;

import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.presentation.MainActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static de.avalax.fitbuddy.runner.TestFitbuddyApplication.TestComponent;

public class FitbuddyActivityTestRule extends ActivityTestRule<MainActivity> {
    @Inject
    EditWorkoutApplicationService editWorkoutApplicationService;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    public FitbuddyActivityTestRule(Class<MainActivity> activityClass) {
        super(activityClass, false, false);
        TestFitbuddyApplication application =
                (TestFitbuddyApplication) getTargetContext().getApplicationContext();
        ((TestComponent) application.getComponent()).inject(this);
    }
}
