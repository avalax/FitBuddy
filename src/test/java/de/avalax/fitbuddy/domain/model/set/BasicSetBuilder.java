package de.avalax.fitbuddy.domain.model.set;

public class BasicSetBuilder {
    private int maxReps;

    public static BasicSetBuilder aSet() {
        return new BasicSetBuilder();
    }

    public BasicSetBuilder withMaxReps(int maxReps) {
        this.maxReps = maxReps;
        return this;
    }

    public Set build() {
        return new BasicSet(null, 0.0, maxReps);
    }
}
