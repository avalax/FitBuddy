package de.avalax.fitbuddy.workout;

interface WorkoutSet {
    String getName();
    int getNumberOfSets();
    Set getSet(int numberOfSet);
    Tendency getTendency();
    void setTendency(Tendency plus);
}
