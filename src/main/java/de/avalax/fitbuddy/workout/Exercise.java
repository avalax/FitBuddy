package de.avalax.fitbuddy.workout;

import java.io.Serializable;

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
    void setTendency(Tendency tendency);
    Tendency getTendency();
    void setCurrentSet(int setNumber);

    Set getCurrentSet();

    void incrementCurrentSet();
}
