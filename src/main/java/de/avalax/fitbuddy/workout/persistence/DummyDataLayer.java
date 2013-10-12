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
        exercises.add(new BasicExercise("Schrägbankdrücken", createSetWithThreeSets(40), 5));
        exercises.add(new BasicExercise("Butterfly", createSetWithThreeSets(60), 5));
        exercises.add(new BasicExercise("Liegestüze", createSetWithThreeSets(0), 12.5));
        exercises.add(new BasicExercise("Lat-Ziehen hinter den Nacken", createSetWithThreeSets(50), 5));
        exercises.add(new BasicExercise("Hochziehen der Langhantel mit enger Handstellung", createSetWithThreeSets(10), 2.5));
        exercises.add(new BasicExercise("Klimmzüge", createSetWithThreeSets(60), 5));
        exercises.add(new BasicExercise("Hammercurls", createSetWithThreeSets(30), 2.5));
        exercises.add(new BasicExercise("Larry Scoot Armbeugen", createSetWithThreeSets(30), 2.5));
        exercises.add(new BasicExercise("Situps", createSetWithThreeSets(0), 2.5));
        return new BasicWorkout(exercises);
    }

    private List<Set> createSetWithThreeSets(double weight) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        sets.add(new BasicSet(weight, 15));
        return sets;
    }
}
