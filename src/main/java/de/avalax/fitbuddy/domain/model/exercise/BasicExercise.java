package de.avalax.fitbuddy.domain.model.exercise;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.set.BasicSets;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;

public class BasicExercise implements Exercise {
    private final BasicSets sets;
    private String name;
    private ExerciseId exerciseId;

    public BasicExercise() {
        this.name = "";
        this.sets = new BasicSets(new ArrayList<>());
    }

    public BasicExercise(ExerciseId exerciseId, String name, List<Set> sets) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.sets = new BasicSets(sets);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getProgress() throws SetException {
        if (sets.size() == 0) {
            return 0;
        }
        Set set = sets.get(sets.indexOfCurrentSet());
        double repsProgress = set.getProgress() / sets.size();
        double setsProgress = (sets.indexOfCurrentSet()) / (double) sets.size();
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Sets getSets() {
        return sets;
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
