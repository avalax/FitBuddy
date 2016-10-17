package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;

public interface Exercises extends Serializable, Iterable<Exercise> {
    Exercise createExercise();

    Exercise get(int index) throws ExerciseException;

    void delete(Exercise exercise);

    int size();

    void setCurrentExercise(int index) throws ExerciseException;

    int indexOfCurrentExercise() throws ExerciseException;

    boolean moveExerciseAtPositionUp(int index) throws ExerciseException;

    boolean moveExerciseAtPositionDown(int index) throws ExerciseException;
}
