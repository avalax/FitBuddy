package de.avalax.fitbuddy.runner;

import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.workout.Workout;

public class PersistenceRunner {
    private FitbuddyActivityTestRule activityRule;

    public PersistenceRunner(FitbuddyActivityTestRule activityRule) {
        this.activityRule = activityRule;
    }

    public void addWorkout(BasicWorkoutBuilder builder) {
        activityRule.editWorkoutApplicationService.saveWorkout(builder.build());
    }

    public void deleteWorkouts() {
        List<Workout> workouts = activityRule.editWorkoutApplicationService.loadAllWorkouts();
        for (Workout workout : workouts) {
            activityRule.editWorkoutApplicationService.deleteWorkout(workout);
        }

        try {
            activityRule.workoutApplicationService.finishCurrentWorkout();
        } catch (ResourceException ignored) {

        }
    }

    public void deleteFinishedWorkouts() {
        List<FinishedWorkout> workouts = activityRule.finishedWorkoutApplicationService.loadAllFinishedWorkouts();
        for (FinishedWorkout finishedWorkout : workouts) {
            activityRule.finishedWorkoutApplicationService.delete(finishedWorkout);
        }
    }
}
