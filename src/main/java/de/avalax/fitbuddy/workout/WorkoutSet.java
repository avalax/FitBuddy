package de.avalax.fitbuddy.workout;

interface WorkoutSet {
    Set getPreviousSet();
    Set getCurrentSet();
    Set getNextSet();
    String getName();
    Tendency getTendency();
    int getNumberOfSets();
    void setTendency(Tendency plus);
    void setSetNumber(int setNumber);
}
