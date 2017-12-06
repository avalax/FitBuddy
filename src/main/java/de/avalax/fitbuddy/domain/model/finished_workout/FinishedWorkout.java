package de.avalax.fitbuddy.domain.model.finished_workout;

import java.io.Serializable;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public interface FinishedWorkout extends Serializable {
    FinishedWorkoutId getFinishedWorkoutId();

    WorkoutId getWorkoutId();

    String getName();

    String getCreated();

    List<FinishedExercise> getFinishedExercises();
}
