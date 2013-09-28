package de.avalax.fitbuddy.workout;

import java.util.List;

public class BasicWorkoutSet implements WorkoutSet {
    private final String name;
    private final int numberOfSets;
    private final List<Set> sets;
    private Tendency tendency;

    public BasicWorkoutSet(String name, int numberOfSets, List<Set> sets) {
        this.name = name;
        this.numberOfSets = numberOfSets;
        this.sets = sets;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfSets() {
        return numberOfSets;
    }

    @Override
    public Set getSet(int numberOfSet) {
        return sets.get(numberOfSet-1);
    }

    @Override
    public Tendency getTendency() {
        return tendency;
    }

    @Override
    public void setTendency(Tendency tendency) {
        this.tendency = tendency;
    }
}
