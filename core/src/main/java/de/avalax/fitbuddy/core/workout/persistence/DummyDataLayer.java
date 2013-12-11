package de.avalax.fitbuddy.core.workout.persistence;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DummyDataLayer implements DataLayer {
    @Override
    public Workout load() {
        LinkedList<Exercise> exercises = new LinkedList<Exercise>();
        exercises.add(new BasicExercise("Bankdrücken", createExerciseWithThreeSets(70, 12), 5));
        exercises.add(new BasicExercise("Schrägbankdrücken", createExerciseWithThreeSets(40, 10), 5));
        exercises.add(new BasicExercise("Butterfly", createExerciseWithThreeSets(60, 15), 5));
        exercises.add(new BasicExercise("Liegestüze", createExerciseWithThreeSets(0, 30), 12.5));
        exercises.add(new BasicExercise("Lat-Ziehen hinter den Nacken", createExerciseWithThreeSets(50, 12), 5));
        exercises.add(new BasicExercise("Hochziehen der Langhantel mit enger Handstellung", createExerciseWithThreeSets(10, 12), 2.5));
        exercises.add(new BasicExercise("Klimmzüge", createExerciseWithThreeSets(60, 8), 5));
        exercises.add(new BasicExercise("Hammercurls", createExerciseWithThreeSets(30, 20), 2.5));
        exercises.add(new BasicExercise("Larry Scoot Armbeugen", createExerciseWithThreeSets(30, 12), 2.5));
        exercises.add(new BasicExercise("Situps", createExerciseWithThreeSets(0, 30), 2.5));
        return new BasicWorkout(exercises);
    }

    private List<Set> createExerciseWithThreeSets(double weight, int reps) {
        List<Set> sets = new ArrayList<Set>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
