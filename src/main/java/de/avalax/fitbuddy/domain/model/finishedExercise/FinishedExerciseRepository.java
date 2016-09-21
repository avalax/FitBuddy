package de.avalax.fitbuddy.domain.model.finishedExercise;

import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;

public interface FinishedExerciseRepository {
    void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise);

    List<FinishedExercise> allSetsBelongsTo(FinishedWorkoutId finishedWorkoutId);
}
