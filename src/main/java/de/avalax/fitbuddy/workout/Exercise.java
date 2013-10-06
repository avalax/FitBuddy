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
    void setRepetitions(int repetitions);
    int getRepetitions();
    void incrementSetNumber();
    double getWeight();
    double getWeightRaise(Tendency plus);
}
