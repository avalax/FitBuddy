package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;

public interface Exercises  extends Serializable, Iterable<Exercise> {
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
}
