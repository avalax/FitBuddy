package de.avalax.fitbuddy.domain.model.finishedWorkout;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.Date;

public interface FinishedWorkout {
    String getWorkoutName();

    FinishedWorkoutId getFinishedWorkoutId();

    String getCreated();

    WorkoutId getWorkoutId();
}
