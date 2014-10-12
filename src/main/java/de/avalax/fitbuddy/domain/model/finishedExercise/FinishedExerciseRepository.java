package de.avalax.fitbuddy.domain.model.finishedExercise;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;

import java.util.List;

public interface FinishedExerciseRepository {
    void save(FinishedWorkoutId finishedWorkoutId, Exercise exercise);

    List<FinishedExercise> allSetsBelongsTo(FinishedWorkoutId finishedWorkoutId);
}
