package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.io.Serializable;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;

public interface FinishedExercise extends Serializable {
    FinishedWorkoutId getFinishedWorkoutId();

    FinishedExerciseId getFinishedExerciseId();

    String getName();

    double getWeight();

    int getReps();

    int getMaxReps();
}
