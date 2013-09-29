package de.avalax.fitbuddy.workout;

public interface WorkoutSet {
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
}
