package de.avalax.fitbuddy.app;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;

import java.util.ArrayList;

public class ExerciseFactory {
    public Exercise createNew() {
        //TODO: create a new exercise with better defaults
        ArrayList<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        return new BasicExercise("new exercise", sets, 0);
    }
}
