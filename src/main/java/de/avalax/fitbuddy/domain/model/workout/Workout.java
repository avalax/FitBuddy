package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import java.io.Serializable;
import java.util.List;

public interface Workout extends Serializable {
    WorkoutId getWorkoutId();

    void setWorkoutId(WorkoutId workoutId);

    void setName(String name);

    String getName();

    double getProgress(int index);

    Exercise createExercise();

    Exercise createExercise(int index);

    void addExercise(int index, Exercise exercise);

    void replaceExercise(Exercise exercise);

    boolean deleteExercise(Exercise exercise);

    List<Exercise> getExercises();
}