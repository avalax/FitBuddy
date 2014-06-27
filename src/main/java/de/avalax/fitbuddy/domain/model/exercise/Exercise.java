package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.Set;

import java.io.Serializable;
import java.util.List;

public interface Exercise extends Serializable {
    String getName();

    double getWeight();

    int indexOfCurrentSet();

    void setReps(int reps);

    int getReps();

    int getMaxReps();

    double getProgress();

    @Deprecated
    void setCurrentSet(int setNumber);

    Set getCurrentSet();

    void setExerciseId(ExerciseId exerciseId);

    ExerciseId getExerciseId();

    List<Set> getSets();

    void setSets(List<Set> sets);

    void setName(String name);
}
