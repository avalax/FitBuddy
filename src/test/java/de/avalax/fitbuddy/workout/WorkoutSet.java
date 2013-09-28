package de.avalax.fitbuddy.workout;

public interface WorkoutSet {
    public String getName();
    public int getNumberOfSets();
    public Set getSet(int numberOfSet);
    public Tendency getTendency();
}
