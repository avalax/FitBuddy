package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;

public class BasicSet implements Set {
    private double weight;
    private int repetitions;

    public BasicSet(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }
}
