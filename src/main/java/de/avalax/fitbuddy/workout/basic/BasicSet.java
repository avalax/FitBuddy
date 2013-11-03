package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;

public class BasicSet implements Set {
    private double weight;
    private int reps;
    private int maxReps;

    public BasicSet(double weight, int maxReps) {
        this.weight = weight;
        this.maxReps = maxReps;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setReps(int reps) {
        if (reps < 0) {
            this.reps = 0;
        } else if (reps > maxReps) {
            this.reps = maxReps;
        } else {
            this.reps = reps;
        }
    }

    @Override
    public int getReps() {
        return reps;
    }

    @Override
    public int getMaxReps() {
        return this.maxReps;
    }
}
