package de.avalax.fitbuddy.workout;

import java.io.Serializable;

public interface Exercise extends Serializable {
    Set getPreviousSet();
    Set getCurrentSet();
    Set getNextSet();
    String getName();
    Tendency getTendency();
    int getSetSize();
    int getSetNumber();
    void setTendency(Tendency tendency);
    void setSetNumber(int setNumber);
    void setReps(int reps);
    int getReps();
    void incrementSet();
    double getWeight();
    double getWeightRaise(Tendency plus);
}
