package de.avalax.fitbuddy.domain.model.workout;

import java.io.Serializable;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;

public interface Workout extends Serializable {
    WorkoutId getWorkoutId();

    void setWorkoutId(WorkoutId workoutId);

    void setName(String name);

    String getName();

    void setLastExecution(Long lastExecution);

    Long getLastExecution();

    double getProgress(int index) throws ResourceException;

    Exercises getExercises();

    void setFinishedCount(int finishedCount);

    int getFinishedCount();
}