package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FakeWorkoutDAO implements WorkoutDAO {

    private ArrayList<Workout> workouts;

    public FakeWorkoutDAO() {
        workouts = new ArrayList<>();
        workouts.add(fakeWorkoutOne());
        workouts.add(fakeWorkoutTwo());
    }

    @Override
    public void save(Workout workout) {
        if (!workouts.contains(workout)) {
            workouts.add(workout);
        }
    }

    @Override
    public Workout load(int position) {
        if (workouts.size() <= position) {
            throw new WorkoutNotAvailableException();
        }
        return workouts.get(position);
    }

    private Workout fakeWorkoutOne() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(new BasicExercise("Bankdrücken", createExerciseWithThreeSets(70, 12), 5));
        exercises.add(new BasicExercise("Schrägbankdrücken", createExerciseWithThreeSets(40, 10), 5));
        exercises.add(new BasicExercise("Butterfly", createExerciseWithThreeSets(60, 15), 5));
        exercises.add(new BasicExercise("Liegestütze", createExerciseWithThreeSets(0, 30), 12.5));
        exercises.add(new BasicExercise("Lat-Ziehen hinter den Nacken", createExerciseWithThreeSets(50, 12), 5));
        exercises.add(new BasicExercise("Hochziehen der Langhantel mit enger Handstellung", createExerciseWithThreeSets(10, 12), 2.5));
        exercises.add(new BasicExercise("Klimmzüge", createExerciseWithThreeSets(60, 8), 5));
        exercises.add(new BasicExercise("Hammercurls", createExerciseWithThreeSets(30, 20), 2.5));
        exercises.add(new BasicExercise("Larry Scoot Armbeugen", createExerciseWithThreeSets(30, 12), 2.5));
        exercises.add(new BasicExercise("Situps", createExerciseWithThreeSets(0, 30), 2.5));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setName("fake workout one");
        return workout;
    }

    private Workout fakeWorkoutTwo() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(new BasicExercise("Bankdrücken", createExerciseWithThreeSets(50, 12), 5));
        exercises.add(new BasicExercise("KH-Rudern", createExerciseWithThreeSets(10, 12), 2.5));
        exercises.add(new BasicExercise("KH-Latissimus", createExerciseWithThreeSets(10, 12), 2.5));
        exercises.add(new BasicExercise("Triceps am hohen Block", createExerciseWithThreeSets(25, 12), 5));
        exercises.add(new BasicExercise("Ausfallschritte", createExerciseWithThreeSets(2, 12), 2));
        exercises.add(new BasicExercise("KH-Nackendrücken", createExerciseWithThreeSets(15, 12), 2.5));
        exercises.add(new BasicExercise("Rückenstrecker", createExerciseWithThreeSets(0, 12), 5));
        exercises.add(new BasicExercise("Crunches am Gerät", createExerciseWithThreeSets(45, 12), 5));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setName("fake workout two");
        return workout;
    }

    @Override
    public List<String> getWorkoutlist() {
        List<String> workoutNames = new ArrayList<>();
        for (Workout workout : workouts) {
            workoutNames.add(workout.getName());
        }
        return workoutNames;
    }

    private List<Set> createExerciseWithThreeSets(double weight, int reps) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
