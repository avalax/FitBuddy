package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.set.SetException;

import java.io.Serializable;

public interface Workout extends Serializable {
    WorkoutId getWorkoutId();

    void setWorkoutId(WorkoutId workoutId);

    void setName(String name);

    String getName();

    double getProgress(int index) throws ExerciseException, SetException;

    Exercise createExercise();

    Exercise createExercise(int index);

    void addExercise(int index, Exercise exercise);

    void replaceExercise(Exercise exercise);

    void deleteExercise(Exercise exercise);

    Exercise exerciseAtPosition(int index) throws ExerciseException;

    void setCurrentExercise(int index) throws ExerciseException;

    int indexOfCurrentExercise() throws ExerciseException;

    boolean moveExerciseAtPositionUp(int index) throws ExerciseException;

    boolean moveExerciseAtPositionDown(int index) throws ExerciseException;

    int countOfExercises();

    Iterable<Exercise> exercisesOfWorkout();
}