package de.avalax.fitbuddy.workout.persistence;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.basic.BasicExercise;
import de.avalax.fitbuddy.workout.basic.BasicSet;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.List;

public class DummyDataLayer implements DataLayer {
    @Override
    public Workout load() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(new BasicExercise("Bankdrücken", createSetWithThreeSets(70), 5));
        exercises.add(new BasicExercise("Situps", createSetWithThreeSets(1.5), 2.5));
        return new BasicWorkout(exercises);
    }

    private List<Set> createSetWithThreeSets(double weight) {
        List<Set> sets = new ArrayList<Set>();
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        return sets;
    }
}
