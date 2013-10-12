package de.avalax.fitbuddy.workout;

import java.io.Serializable;

public interface Exercise extends Serializable {
    String getName();
    double getWeight();
    double getWeightRaise(Tendency plus);
    int getSetSize();
    int getSetNumber();
    void setReps(int reps);
    int getReps();
    void setTendency(Tendency tendency);
    Tendency getTendency();
    void setCurrentSet(int setNumber);
    Set getCurrentSet();
    void incrementCurrentSet();
}
