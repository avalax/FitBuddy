package de.avalax.fitbuddy.domain.model.finishedWorkout;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.Date;

public interface FinishedWorkout {
    FinishedWorkoutId getFinishedWorkoutId();

    WorkoutId getWorkoutId();

    String getName();

    String getCreated();
}
