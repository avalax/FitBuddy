package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.LinkedList;

public interface ExerciseRepository {

    void save(WorkoutId id, int position, Exercise exercise);

    void delete(ExerciseId id);

    LinkedList<Exercise> allExercisesBelongsTo(WorkoutId workoutId);
}
