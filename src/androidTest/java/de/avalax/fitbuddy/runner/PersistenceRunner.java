package de.avalax.fitbuddy.runner;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.workout.Workout;

public class PersistenceRunner {
    private FitbuddyActivityTestRule activityRule;

    public PersistenceRunner(FitbuddyActivityTestRule activityRule) {
        this.activityRule = activityRule;
    }

    public Workout addWorkout(BasicWorkoutBuilder builder) throws ResourceException {
        Workout workout = builder.build();
        activityRule.editWorkoutService.saveWorkout(workout);
        return workout;
    }

    public FinishedWorkoutId finishWorkout(Workout workout) throws ResourceException {
        activityRule.workoutService.switchWorkout(workout);
        return activityRule.workoutService.finishCurrentWorkout();
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

    public void updateFinishedWorkoutCreation(FinishedWorkoutId firstFinishedWorkoutId, String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date dateDate = dateFormat.parse(date);
        activityRule.finishedWorkoutService.updatedCreation(firstFinishedWorkoutId, dateDate);
    }

    public void deleteFinishedWorkouts() {
        List<FinishedWorkout> workouts = activityRule.finishedWorkoutService.loadAllFinishedWorkouts();
        for (FinishedWorkout finishedWorkout : workouts) {
            activityRule.finishedWorkoutService.delete(finishedWorkout);
        }
    }

    public void setPaid() {
        activityRule.billingProvider.purchase();
    }

    public void deleteSharedPreferences() {
        SharedPreferences preferences = activityRule.getActivity().getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}
