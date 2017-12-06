package de.avalax.fitbuddy.domain.model.finished_workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.workout.Workout;

public interface FinishedWorkoutRepository {
    FinishedWorkoutId saveWorkout(Workout workout);

    FinishedWorkout load(FinishedWorkoutId finishedWorkoutId) throws FinishedWorkoutException;

    List<FinishedWorkout> loadAll();

    void delete(FinishedWorkoutId finishedWorkoutId);

    long size();
}
