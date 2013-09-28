package de.avalax.fitbuddy.workout;

import java.util.List;

public class BasicWorkoutSet implements WorkoutSet {
    private final String name;
    private final List<Set> sets;
    private Tendency tendency;
    private int setNumber;

    public BasicWorkoutSet(String name, List<Set> sets) {
        this.name = name;
        this.sets = sets;
        this.setNumber = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfSets() {
        return sets.size();
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
    public Set getPreviousSet() {
        int index = previousSetIndex();
        if (isValid(index)) {
            throw new SetNotAvailableException();
        }
        return sets.get(index);
    }

    @Override
    public Set getNextSet() {
        int index = nextSetIndex();
        if (isValid(index)){
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
        return index < 0 || index > sets.size()-1;
    }
}
