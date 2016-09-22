package de.avalax.fitbuddy.domain.model.finished_exercise;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;

public interface FinishedExercise {
    FinishedWorkoutId getFinishedWorkoutId();

    FinishedExerciseId getFinishedExerciseId();

    String getName();

    double getWeight();

    int getReps();

    int getMaxReps();
}
