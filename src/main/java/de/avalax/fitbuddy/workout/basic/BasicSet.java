package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;

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
        this.repetitions = repetitions;
    }

    @Override
    public int getRepetitions() {
        return repetitions;
    }

	@Override
	public void setMaxRepetitions(int maxRepetitions){
		this.maxRepetitions = maxRepetitions;
	}

	@Override
	public int getMaxRepetition(){
		return this.maxRepetitions;
	}
}
