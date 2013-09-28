package de.avalax.fitbuddy.workout;

interface WorkoutSet {
    Set getPreviousSet();
    Set getCurrentSet();
    Set getNextSet();
    String getName();
    Tendency getTendency();
    int getSetSize();
    int getSetNumber();
    void setTendency(Tendency plus);
    void setSetNumber(int setNumber);

    void setRepetitions(int repetitions);
}
