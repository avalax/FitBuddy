package de.avalax.fitbuddy.workout.persistence;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.basic.BasicExercise;
import de.avalax.fitbuddy.workout.basic.BasicSet;
import de.avalax.fitbuddy.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DummyDataLayer implements DataLayer {
    @Override
    public Workout load() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(new BasicExercise("Bankdrücken", createSetWithThreeSets(70,12), 5));
        exercises.add(new BasicExercise("Schrägbankdrücken", createSetWithThreeSets(40,10), 5));
        exercises.add(new BasicExercise("Butterfly", createSetWithThreeSets(60,15), 5));
        exercises.add(new BasicExercise("Liegestüze", createSetWithThreeSets(0,30), 12.5));
        exercises.add(new BasicExercise("Lat-Ziehen hinter den Nacken", createSetWithThreeSets(50,12), 5));
        exercises.add(new BasicExercise("Hochziehen der Langhantel mit enger Handstellung", createSetWithThreeSets(10,12), 2.5));
        exercises.add(new BasicExercise("Klimmzüge", createSetWithThreeSets(60,8), 5));
        exercises.add(new BasicExercise("Hammercurls", createSetWithThreeSets(30,20), 2.5));
        exercises.add(new BasicExercise("Larry Scoot Armbeugen", createSetWithThreeSets(30,12), 2.5));
        exercises.add(new BasicExercise("Situps", createSetWithThreeSets(0,30), 2.5));
        return new BasicWorkout(exercises);
    }

    private List<Set> createSetWithThreeSets(double weight,int reps) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
