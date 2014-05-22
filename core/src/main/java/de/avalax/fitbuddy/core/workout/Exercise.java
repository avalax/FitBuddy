package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;
import java.util.List;

public interface Exercise extends Serializable {
    String getName();
    double getWeight();
    double getWeightRaise();
    double getWeightRaise(Tendency plus);
    int getMaxSets();
    int getSetNumber();
    void setReps(int reps);
    int getReps();
    int getMaxReps();
    double getProgress();
    void setTendency(Tendency tendency);
    Tendency getTendency();
    void setCurrentSet(int setNumber);

    Set getCurrentSet();

    void incrementCurrentSet();

    void setId(Long id);
    Long getId();

    List<Set> getSets();
}
