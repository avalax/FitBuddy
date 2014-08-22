package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;

import java.io.Serializable;

public interface Exercise extends Serializable {
    String getName();

    int indexOfCurrentSet();

    double getProgress();

    void setCurrentSet(int index);

    Set setAtPosition(int index) throws SetNotAvailableException;

    void setExerciseId(ExerciseId exerciseId);

    int countOfSets();

    ExerciseId getExerciseId();

    void setName(String name);

    @Deprecated
    void addSet(Set set);

    void removeSet(Set set);
}
