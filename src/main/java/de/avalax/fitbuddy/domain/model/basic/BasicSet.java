package de.avalax.fitbuddy.domain.model.basic;

import de.avalax.fitbuddy.domain.model.Set;
import de.avalax.fitbuddy.domain.model.SetId;

public class BasicSet implements Set {
    private double weight;
    private int reps;
    private int maxReps;
    private SetId setId;

    public BasicSet(double weight, int maxReps) {
        this.weight = weight;
        this.maxReps = maxReps;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public SetId getSetId() {
        return setId;
    }

    @Override
    public void setId(SetId setId) {

        this.setId = setId;
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

    @Override
    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }


    @Override
    public boolean equals(Object o) {
        if (setId == null) {
            return super.equals(o);
        }
        return o instanceof BasicSet && setId.equals(((BasicSet) o).setId);
    }

    @Override
    public int hashCode() {
        if (setId == null) {
            return super.hashCode();
        }
        return setId.hashCode();
    }
}
