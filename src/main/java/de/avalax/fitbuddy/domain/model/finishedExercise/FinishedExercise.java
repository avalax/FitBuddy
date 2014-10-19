package de.avalax.fitbuddy.domain.model.finishedExercise;

import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;

public interface FinishedExercise {
    FinishedWorkoutId getFinishedWorkoutId();

    FinishedExerciseId getFinishedExerciseId();

    String getName();

    double getWeight();

    int getReps();

    int getMaxReps();
}
