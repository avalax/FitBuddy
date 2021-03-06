package de.avalax.fitbuddy.domain.model.set;

public class BasicSet implements Set {
    private double weight;
    private int reps;
    private int maxReps;
    private SetId setId;

    public BasicSet() {
        this.maxReps = 1;
        this.weight = 0;
    }

    public BasicSet(SetId setId, double weight, int maxReps) {
        this.setId = setId;
        setWeight(weight);
        setMaxReps(maxReps);
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
    public void setSetId(SetId setId) {

        this.setId = setId;
    }

    @Override
    public double getProgress() {
        return (double) reps / maxReps;
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
    public boolean equals(Object obj) {
        if (setId == null) {
            return super.equals(obj);
        }
        return obj instanceof Set && setId.equals(((Set) obj).getSetId());
    }

    @Override
    public int hashCode() {
        if (setId == null) {
            return super.hashCode();
        }
        return setId.hashCode();
    }

    @Override
    public String toString() {
        if (setId == null) {
            return "BasicSet [weight=" + weight + ", maxReps=" + maxReps + "]";
        }
        return "BasicSet [weight=" + weight + ", maxReps=" + maxReps + ", setId=" + setId + "]";
    }
}
