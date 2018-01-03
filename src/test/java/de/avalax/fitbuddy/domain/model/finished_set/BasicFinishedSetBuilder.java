package de.avalax.fitbuddy.domain.model.finished_set;

public class BasicFinishedSetBuilder {
    private int maxReps;

    public static BasicFinishedSetBuilder aFinishedSet() {
        return new BasicFinishedSetBuilder();
    }

    public BasicFinishedSetBuilder withMaxReps(int maxReps) {
        this.maxReps = maxReps;
        return this;
    }

    public FinishedSet build() {
        return new BasicFinishedSet(0, 0, maxReps);
    }
}
