package de.avalax.fitbuddy.domain.model.finished_workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public interface FinishedWorkoutRepository {
    FinishedWorkoutId saveWorkout(Workout workout) throws WorkoutException;

    FinishedWorkout load(FinishedWorkoutId finishedWorkoutId) throws FinishedWorkoutException;

    List<FinishedWorkout> loadAll();

    void delete(FinishedWorkoutId finishedWorkoutId);

    long size();

    long count(WorkoutId workoutId);

    Long lastCreation(WorkoutId workoutId);
}
