package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;
import java.util.Collection;

public interface Exercises extends Serializable, Iterable<Exercise> {
    Exercise createExercise();

    @Deprecated
        // Remove exception
    Exercise get(int index) throws ExerciseException;

    int size();

    void setCurrentExercise(int index) throws ExerciseException;

    @Deprecated
    // remove exception
    int indexOfCurrentExercise() throws ExerciseException;

    boolean moveExerciseAtPositionUp(int index) throws ExerciseException;

    boolean moveExerciseAtPositionDown(int index) throws ExerciseException;

    void add(Exercise exercise);

    void set(int position, Exercise exercise);

    void removeAll(Collection<Exercise> exercises);
}
