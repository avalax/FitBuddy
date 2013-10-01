package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.WorkoutSet;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;

import java.util.List;

public class BasicWorkoutSet implements WorkoutSet {
    private final String name;
    private final List<Set> sets;
    private double weightRaise;
    private Tendency tendency;
    private int setNumber;

    public BasicWorkoutSet(String name, List<Set> sets, double weightRaise) {
        this.name = name;
        this.sets = sets;
        this.weightRaise = weightRaise;
        this.setNumber = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSetSize() {
        return sets.size();
    }

    @Override
    public int getSetNumber() {
        return setNumber;
    }

    @Override
    public Tendency getTendency() {
        return tendency;
    }

    @Override
    public void setTendency(Tendency tendency) {
        this.tendency = tendency;
    }

    @Override
    public Set getCurrentSet() {
        return sets.get(currentSetIndex());
    }

    @Override
    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    @Override
    public void setRepetitions(int repetitions) {
        getCurrentSet().setRepetitions(repetitions);
        incrementSetNumber();
    }

    @Override
    public int getRepetitions() {
        return getCurrentSet().getRepetitions();
    }

    public void incrementSetNumber() {
        if(isValid(nextSetIndex())){
            setNumber++;
        }
        else{
            throw new SetNotAvailableException();
        }
    }

    @Override
    public double getWeight() {
        return getCurrentSet().getWeight();
    }

    @Override
    public double getWeightRaise() {
        return weightRaise;
    }

    @Override
    public Set getPreviousSet() {
        int index = previousSetIndex();
        if (!isValid(index)) {
            throw new SetNotAvailableException();
        }
        return sets.get(index);
    }

    @Override
    public Set getNextSet() {
        int index = nextSetIndex();
        if (!isValid(index)){
            throw new SetNotAvailableException();
        }
        return sets.get(index);
    }

    private int previousSetIndex() {
        return currentSetIndex() - 1;
    }

    private int currentSetIndex() {
        return setNumber - 1;
    }

    private int nextSetIndex() {
        return currentSetIndex()+1;
    }

    private boolean isValid(int index) {
        return isIndexNotNegative(index) && isIndexInArray(index);
    }

    private boolean isIndexInArray(int index) {
        return index <= sets.size()-1;
    }

    private boolean isIndexNotNegative(int index) {
        return index >= 0;
    }
}
