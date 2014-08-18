package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.Set;

import java.io.Serializable;
import java.util.List;

public interface Exercise extends Serializable {
    String getName();

    int indexOfCurrentSet();

    double getProgress();

    void setCurrentSet(int index);

    @Deprecated
    Set getCurrentSet();

    void setExerciseId(ExerciseId exerciseId);

    ExerciseId getExerciseId();

    List<Set> getSets();

    void setName(String name);

    @Deprecated
    void addSet(Set set);

    void removeSet(Set set);
}
