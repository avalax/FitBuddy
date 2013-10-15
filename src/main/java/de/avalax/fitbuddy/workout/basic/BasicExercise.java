package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;

import java.util.List;

public class BasicExercise implements Exercise {
    private final String name;
    private final List<Set> sets;
    private double weightRaise;
    private Tendency tendency;
    private int setNumber;

    public BasicExercise(String name, List<Set> sets, double weightRaise) {
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
    public void setCurrentSet(int setNumber) {
        if (isIndexNotInArray(setNumber - 1)) {
            this.setNumber = getSetSize();
        } else if (isIndexNegative(setNumber - 1)) {
            this.setNumber = 1;
        } else {
            this.setNumber = setNumber;
        }
    }

    @Override
    public void setReps(int reps) {
        getCurrentSet().setReps(reps);
    }

    @Override
    public int getReps() {
        return getCurrentSet().getReps();
    }

    public void incrementCurrentSet() {
        setCurrentSet(setNumber + 1);
    }

    @Override
    public double getWeight() {
        return getCurrentSet().getWeight();
    }

    @Override
    public double getWeightRaise(Tendency tendency) {
        double weight = getCurrentSet().getWeight();
        switch (tendency) {
            case MINUS:
                return getMinusTendencyWeight(weight);
            case PLUS:
                return getPlusTendencyWeight(weight);
            default:
                return weight;
        }
    }

    private int currentSetIndex() {
        return setNumber - 1;
    }

    private boolean isIndexNotInArray(int index) {
        return index >= sets.size();
    }

    private boolean isIndexNegative(int index) {
        return index < 0;
    }

    private double getPlusTendencyWeight(double weight) {
        return weight + weightRaise;
    }

    private double getMinusTendencyWeight(double weight) {
        if (weight - weightRaise < 0) {
            return 0;
        }
        return weight - weightRaise;
    }
}
