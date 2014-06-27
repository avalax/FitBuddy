package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicExercise implements Exercise {
    private String name;
    private List<Set> sets;
    private int exerciseIndex;
    private ExerciseId exerciseId;

    public BasicExercise() {
        this.name = "";
        this.sets = new ArrayList<>();
    }

    public BasicExercise(String name, List<Set> sets) {
        this.name = name;
        this.sets = sets;
        this.exerciseIndex = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int indexOfCurrentSet() {
        return exerciseIndex;
    }

    @Override
    public Set getCurrentSet() {
        if (isIndexGreaterEqualThan(exerciseIndex)) {
            throw new SetNotAvailableException();
        }
        int index = exerciseIndex;
        return sets.get(index);
    }

    @Override
    public void setCurrentSet(int index) {
        if (sets.isEmpty()) {
            return;
        }
        if (isIndexGreaterEqualThan(index)) {
            this.exerciseIndex = sets.size() - 1;
        } else if (isIndexNegative(index)) {
            this.exerciseIndex = 0;
        } else {
            this.exerciseIndex = index;
        }
    }

    @Override
    public double getProgress() {
        double setsProgress = (exerciseIndex) / (double) sets.size();
        //TODO: add getProgress to Set
        double repsProgress = getCurrentSet().getReps() / (double) getCurrentSet().getMaxReps() / sets.size();
        return setsProgress + repsProgress;
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
        return Collections.unmodifiableList(sets);
    }

    @Override
    public void setSets(List<Set> sets) {
        this.sets = sets;
        this.exerciseIndex = 0;
    }

    @Override
    public void setName(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name.trim();
        }
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
