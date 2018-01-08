package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.List;

public interface ExerciseRepository {

    void save(WorkoutId id, Exercise exercise);

    void delete(ExerciseId id);

    List<Exercise> allExercisesBelongsTo(WorkoutId workoutId);
}
