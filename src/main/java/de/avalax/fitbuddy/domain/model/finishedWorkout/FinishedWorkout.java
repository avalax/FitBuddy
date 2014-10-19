package de.avalax.fitbuddy.domain.model.finishedWorkout;

import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.List;

public interface FinishedWorkout {
    FinishedWorkoutId getFinishedWorkoutId();

    WorkoutId getWorkoutId();

    String getName();

    String getCreated();

    List<FinishedExercise> getFinishedExercises();
}
