package de.avalax.fitbuddy.domain.model.basic;

import de.avalax.fitbuddy.domain.model.Exercise;
import de.avalax.fitbuddy.domain.model.ExerciseId;
import de.avalax.fitbuddy.domain.model.Set;
import de.avalax.fitbuddy.domain.model.exceptions.SetNotAvailableException;

import java.util.List;

public class BasicExercise implements Exercise {
    private String name;
    private List<Set> sets;
    private int setNumber;
    private ExerciseId exerciseId;

    public BasicExercise(String name, List<Set> sets) {
        this.name = name;
        this.sets = sets;
        this.setNumber = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxSets() {
        return sets.size();
    }

    @Override
    public int getSetNumber() {
        return setNumber;
    }

    @Override
    public Set getCurrentSet() {
        if (setNumber > sets.size()) {
            throw new SetNotAvailableException();
        }
        int index = setNumber - 1;
        return sets.get(index);
    }

    @Override
    public void setCurrentSet(int setNumber) {
        int index = setNumber - 1;
        if (isIndexGreaterEqualThan(index)) {
            this.setNumber = getMaxSets();
        } else if (isIndexNegative(index)) {
            this.setNumber = 1;
        } else {
            this.setNumber = setNumber;
        }
    }

    @Override
    public int getReps() {
        return getCurrentSet().getReps();
    }

    @Override
    public void setReps(int reps) {
        getCurrentSet().setReps(reps);
    }

    @Override
    public int getMaxReps() {
        return getCurrentSet().getMaxReps();
    }

    @Override
    public double getProgress() {
        double setsProgress = (setNumber - 1) / (double) getMaxSets();
        double repsProgress = getReps() / (double) getMaxReps() / getMaxSets();
        return setsProgress + repsProgress;
    }

    public void incrementCurrentSet() {
        setCurrentSet(setNumber + 1);
    }

    @Override
    public void setExerciseId(ExerciseId exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public ExerciseId getExerciseId() {
        return exerciseId;
    }

    @Override
    public List<Set> getSets() {
        return sets;
    }

    @Override
    public void setSets(List<Set> sets) {
        this.sets = sets;
        this.setNumber = 1;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getWeight() {
        return getCurrentSet().getWeight();
    }

    private boolean isIndexGreaterEqualThan(int index) {
        return index >= sets.size();
    }

    private boolean isIndexNegative(int index) {
        return index < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (exerciseId == null) {
            return super.equals(o);
        }
        return o instanceof BasicExercise && exerciseId.equals(((BasicExercise) o).exerciseId);
    }

    @Override
    public int hashCode() {
        if (exerciseId == null) {
            return super.hashCode();
        }
        return exerciseId.hashCode();
    }
}
