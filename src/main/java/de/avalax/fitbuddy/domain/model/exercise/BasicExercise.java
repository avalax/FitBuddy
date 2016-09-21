package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicExercise implements Exercise {
    private String name;
    private List<Set> sets;
    private int setIndex;
    private ExerciseId exerciseId;

    public BasicExercise() {
        this.name = "";
        this.sets = new ArrayList<>();
    }

    public BasicExercise(ExerciseId exerciseId, String name, List<Set> sets) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.sets = sets;
        this.setIndex = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int indexOfCurrentSet() throws SetException {
        if (sets.isEmpty()) {
            throw new SetException();
        }
        return setIndex;
    }

    @Override
    public Set setAtPosition(int position) throws SetException {
        if (sets.size() <= position || position < 0) {
            throw new SetException();
        }
        return sets.get(position);
    }

    @Override
    public void setCurrentSet(int position) throws SetException {
        if (sets.size() <= position || position < 0) {
            throw new SetException();
        }
        this.setIndex = position;
    }

    @Override
    public double getProgress() throws SetException {
        if (sets.isEmpty()) {
            return 0;
        }
        Set set = setAtPosition(setIndex);
        double repsProgress = set.getProgress() / sets.size();
        double setsProgress = (setIndex) / (double) sets.size();
        return setsProgress + repsProgress;
    }

    @Override
    public void setExerciseId(ExerciseId exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public int countOfSets() {
        return sets.size();
    }

    @Override
    public ExerciseId getExerciseId() {
        return exerciseId;
    }

    @Override
    public void setName(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name.trim();
        }
    }

    @Override
    public Set createSet() {
        Set set = new BasicSet();
        sets.add(set);
        return set;
    }

    @Override
    public void removeSet(Set set) {
        sets.remove(set);
    }

    @Override
    public Iterable<Set> setsOfExercise() {
        return Collections.unmodifiableList(sets);
    }

    @Override
    public boolean equals(Object o) {
        if (exerciseId == null) {
            return super.equals(o);
        }
        return o instanceof Exercise && exerciseId.equals(((Exercise) o).getExerciseId());
    }

    @Override
    public int hashCode() {
        if (exerciseId == null) {
            return super.hashCode();
        }
        return exerciseId.hashCode();
    }

    @Override
    public String toString() {
        if (exerciseId == null) {
            return "BasicExercise [name=" + name + "]";
        }
        return "BasicExercise [name=" + name + ", exerciseId=" + exerciseId.toString() + "]";
    }
}
