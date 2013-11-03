package de.avalax.fitbuddy.edit;

public class NewEditableExercise implements EditableExercise {
    private int reps;
    private int sets;
    private double weight;
    private double weightRaise;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public int getReps() {
        return reps;
    }

    @Override
    public void setReps(int reps) {
        this.reps = reps;
    }

    @Override
    public int getSets() {
        return sets;
    }

    @Override
    public void setSets(int sets) {
        this.sets = sets;
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
    public double getWeightRaise() {
        return weightRaise;
    }

    @Override
    public void setWeightRaise(double weightRaise) {
        this.weightRaise = weightRaise;
    }
}
