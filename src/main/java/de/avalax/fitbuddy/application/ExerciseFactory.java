package de.avalax.fitbuddy.application;

import de.avalax.fitbuddy.domain.model.Exercise;
import de.avalax.fitbuddy.domain.model.Set;
import de.avalax.fitbuddy.domain.model.basic.BasicExercise;
import de.avalax.fitbuddy.domain.model.basic.BasicSet;

import java.util.ArrayList;

public class ExerciseFactory {
    public Exercise createNew() {
        //TODO: create a new exercise with better defaults
        ArrayList<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        return new BasicExercise("new exercise", sets);
    }
}
