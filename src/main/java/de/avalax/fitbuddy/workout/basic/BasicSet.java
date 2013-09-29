package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.exceptions.RepetitionsExceededException;

public class BasicSet implements Set {
    private double weight;
    private int repetitions;
		private int maxRepetitions;

	public BasicSet(double weight, int maxRepetitions) {
        this.weight = weight;
				this.maxRepetitions = maxRepetitions;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setRepetitions(int repetitions) {
        if (repetitions > maxRepetitions) {
            throw new RepetitionsExceededException();
        }
        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }

	@Override
	public int getMaxRepetition(){
		return this.maxRepetitions;
	}
}
