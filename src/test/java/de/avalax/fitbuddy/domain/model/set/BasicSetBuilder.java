package de.avalax.fitbuddy.domain.model.set;

public class BasicSetBuilder {
    private int maxReps;
    private double weight;

    public static BasicSetBuilder aSet() {
        return new BasicSetBuilder();
    }

    public BasicSetBuilder withMaxReps(int maxReps) {
        this.maxReps = maxReps;
        return this;
    }

    public Set build() {
        return new BasicSet(null, weight, maxReps);
    }

    public BasicSetBuilder withWeight(double weight) {
        this.weight = weight;
        return this;
    }
}
