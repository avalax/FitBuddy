package de.avalax.fitbuddy.domain.model.finished_workout;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.List;

public interface FinishedWorkout {
    FinishedWorkoutId getFinishedWorkoutId();

    WorkoutId getWorkoutId();

    String getName();

    String getCreated();

    List<FinishedExercise> getFinishedExercises();
}
