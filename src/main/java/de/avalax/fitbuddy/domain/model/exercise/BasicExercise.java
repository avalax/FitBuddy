package de.avalax.fitbuddy.domain.model.exercise;

import android.util.Log;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;

import java.util.ArrayList;
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
    public int indexOfCurrentSet() {
        return setIndex;
    }

    @Override
    public Set setAtPosition(int position) throws SetNotAvailableException {
        if (isIndexGreaterEqualThan(position)) {
            throw new SetNotAvailableException();
        }
        return sets.get(position);
    }

    @Override
    public void setCurrentSet(int index) {
        if (sets.isEmpty()) {
            return;
        }
        if (isIndexGreaterEqualThan(index)) {
            this.setIndex = sets.size() - 1;
        } else if (isIndexNegative(index)) {
            this.setIndex = 0;
        } else {
            this.setIndex = index;
        }
    }

    @Override
    public double getProgress() {
        if (sets.isEmpty()) {
            return 0;
        }
        try {
            //TODO: add getProgress to Set
            int reps = setAtPosition(setIndex).getReps();
            double maxReps = setAtPosition(setIndex).getMaxReps();
            double repsProgress = reps / maxReps / sets.size();
            double setsProgress = (setIndex) / (double) sets.size();
            return setsProgress + repsProgress;
        } catch (SetNotAvailableException e) {
            Log.d("no sets available", e.getMessage(), e);
            return 0;
        }
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
