package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.exceptions.RepsExceededException;

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
        if (reps > maxReps) {
            throw new RepsExceededException();
        }
        this.reps = reps;
    }

    @Override
    public int getReps() {
        return reps;
    }

	@Override
	public int getMaxReps(){
		return this.maxReps;
	}
}
