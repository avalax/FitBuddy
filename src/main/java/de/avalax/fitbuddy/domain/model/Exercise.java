package de.avalax.fitbuddy.domain.model;

import java.io.Serializable;
import java.util.List;

public interface Exercise extends Serializable {
    String getName();

    double getWeight();

    int getMaxSets();

    int getSetNumber();

    void setReps(int reps);

    int getReps();

    int getMaxReps();

    double getProgress();

    void setCurrentSet(int setNumber);

    Set getCurrentSet();

    void incrementCurrentSet();

    void setExerciseId(ExerciseId exerciseId);

    ExerciseId getExerciseId();

    List<Set> getSets();

    void setSets(List<Set> sets);

    void setName(String name);
}
