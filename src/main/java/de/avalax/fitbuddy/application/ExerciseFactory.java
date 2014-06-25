package de.avalax.fitbuddy.application;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.set.BasicSet;

import java.util.ArrayList;

public class ExerciseFactory {
    public Exercise createNew() {
        //TODO: create a new exercise with better defaults
        ArrayList<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        return new BasicExercise("", sets);
    }
}
