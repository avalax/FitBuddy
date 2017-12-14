package de.avalax.fitbuddy.domain.model.set;

public class BasicSetBuilder {
    private double weight = 0;
    private int reps = 1;

    public static BasicSetBuilder aSet() {
        return new BasicSetBuilder();
    }

    public Set build() {
        Set set = new BasicSet();
        set.setWeight(weight);
        set.setMaxReps(reps);
        return set;
    }

    public BasicSetBuilder withWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public BasicSetBuilder withMaxReps(int reps) {
        this.reps = reps;
        return this;
    }
}
