package de.avalax.fitbuddy.domain.model.finished_set;

public class BasicFinishedSet implements FinishedSet {

    private final double weight;
    private final int reps;
    private final int maxReps;

    public BasicFinishedSet(double weight, int reps, int maxReps) {
        this.weight = weight;
        this.reps = reps;
        this.maxReps = maxReps;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int getReps() {
        return reps;
    }

    @Override
    public int getMaxReps() {
        return maxReps;
    }

    @Override
    public String toString() {
        return "BasicFinishedSet ["
                + "reps=" + reps
                + ", maxReps=" + maxReps
                + ", weight=" + weight
                + "]";
    }
}
