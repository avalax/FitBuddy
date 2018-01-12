package de.avalax.fitbuddy.runner;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;

public class PersistenceRunner {
    private FitbuddyActivityTestRule activityRule;

    public PersistenceRunner(FitbuddyActivityTestRule activityRule) {
        this.activityRule = activityRule;
    }

    public Workout addWorkout(BasicWorkoutBuilder builder) throws WorkoutException {
        Workout workout = builder.build();
        activityRule.editWorkoutService.saveWorkout(workout);
        return workout;
    }

    public void finishWorkout(Workout workout) throws ResourceException {
        activityRule.workoutService.switchWorkout(workout);
        activityRule.workoutService.finishCurrentWorkout();
    }

    public void deleteWorkouts() throws ResourceException {
        List<Workout> workouts = activityRule.editWorkoutService.loadAllWorkouts();
        for (Workout workout : workouts) {
            activityRule.editWorkoutService.deleteWorkout(workout.getWorkoutId());
        }

        if (activityRule.workoutService.hasActiveWorkout()) {
            activityRule.workoutService.finishCurrentWorkout();
        }
    }

    public void deleteFinishedWorkouts() {
        List<FinishedWorkout> workouts = activityRule.finishedWorkoutService.loadAllFinishedWorkouts();
        for (FinishedWorkout finishedWorkout : workouts) {
            activityRule.finishedWorkoutService.delete(finishedWorkout);
        }
    }

    public void setPaid() {
        activityRule.adMobProvider.setPaid();
    }

    public void deleteSharedPreferences() {
        SharedPreferences preferences = activityRule.getActivity().getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}
