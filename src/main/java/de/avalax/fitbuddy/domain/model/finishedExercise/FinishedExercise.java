package de.avalax.fitbuddy.domain.model.finishedExercise;

import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public interface FinishedExercise {
    FinishedWorkoutId getFinishedWorkoutId();

    FinishedExerciseId getFinishedExerciseId();

    String getName();

    double getWeight();

    int getReps();
}
