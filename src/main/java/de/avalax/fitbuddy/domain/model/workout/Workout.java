package de.avalax.fitbuddy.domain.model.workout;

import java.io.Serializable;
import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;

public interface Workout extends Serializable {
    WorkoutId getWorkoutId();

    void setWorkoutId(WorkoutId workoutId);

    void setName(String name);

    String getName();

    double getProgress(int index) throws ResourceException;

    Exercise createExercise();

    Exercise createExercise(int index);

    void addExercise(int index, Exercise exercise);

    void deleteExercise(Exercise exercise);

    Exercise exerciseAtPosition(int index) throws ExerciseException;

    void setCurrentExercise(int index) throws ExerciseException;

    int indexOfCurrentExercise() throws ExerciseException;

    boolean moveExerciseAtPositionUp(int index) throws ExerciseException;

    boolean moveExerciseAtPositionDown(int index) throws ExerciseException;

    int countOfExercises();

    List<Exercise> exercisesOfWorkout();
}