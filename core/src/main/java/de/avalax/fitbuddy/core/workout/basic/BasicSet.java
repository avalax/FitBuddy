package de.avalax.fitbuddy.core.workout.basic;

import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.SetId;

public class BasicSet implements Set {
    private double weight;
    private int reps;
    private int maxReps;
    private SetId id;

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
    public SetId getId() {
        return id;
    }

    @Override
    public void setId(SetId id) {

        this.id = id;
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
    public boolean equals(Object o) {
        if (id == null) {
            return super.equals(o);
        }
        return o instanceof BasicSet && id.equals(((BasicSet) o).id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }
}
