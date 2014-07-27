package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.List;

public interface ExerciseRepository {

    void save(WorkoutId id, int position, Exercise exercise);

    void delete(ExerciseId id);

    List<Exercise> allExercisesBelongsTo(WorkoutId workoutId);

    Exercise loadExerciseFromWorkoutWithPosition(WorkoutId workoutId, int position) throws ExerciseNotFoundException;
}
