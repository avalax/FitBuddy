package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;

public interface FinishedExerciseRepository {
    void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise);

    List<FinishedExercise> allExercisesBelongsTo(FinishedWorkoutId finishedWorkoutId);
}
