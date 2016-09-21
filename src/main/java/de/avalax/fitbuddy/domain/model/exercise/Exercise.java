package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;

import java.io.Serializable;

public interface Exercise extends Serializable {
    String getName();

    int indexOfCurrentSet() throws SetException;

    double getProgress() throws SetException;

    void setCurrentSet(int index) throws SetException;

    Set setAtPosition(int index) throws SetException;

    void setExerciseId(ExerciseId exerciseId);

    int countOfSets();

    ExerciseId getExerciseId();

    void setName(String name);

    Set createSet();

    void removeSet(Set set);

    Iterable<Set> setsOfExercise();
}
