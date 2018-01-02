package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.io.Serializable;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;

public interface FinishedExercise extends Serializable {
    FinishedWorkoutId getFinishedWorkoutId();

    FinishedExerciseId getFinishedExerciseId();

    String getName();

    List<FinishedSet> getSets();
}
