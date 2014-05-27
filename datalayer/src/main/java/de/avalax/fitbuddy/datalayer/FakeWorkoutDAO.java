package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.*;
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
            workout.setId(new WorkoutId((long) (Math.random() * 100000)));
        }
    }

    @Override
    public void saveExercise(WorkoutId id, Exercise exercise) {
        // FAKE
    }

    @Override
    public void deleteExercise(ExerciseId id) {
        // FAKE
    }

    @Override
    public void saveSet(ExerciseId id, Set set) {
        // FAKE
    }

    @Override
    public void deleteSet(SetId id) {
        // FAKE
    }

    @Override
    public Workout load(WorkoutId id) {
        for (Workout workout : workouts) {
            if (workout.getId().equals(id)) {
                return workout;
            }
        }
        throw new WorkoutNotAvailableException();
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
        workout.setId(new WorkoutId(0));
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
        workout.setId(new WorkoutId(1));
        workout.setName("fake workout two");
        return workout;
    }

    @Override
    public List<Workout> getList() {
        List<Workout> workoutList = new ArrayList<>();

        for (Workout workout : workouts) {
            Workout w = new BasicWorkout(new LinkedList<Exercise>());
            w.setId(workout.getId());
            w.setName(workout.getName());
            workoutList.add(w);
        }
        return workoutList;
    }

    @Override
    public void delete(WorkoutId id) {
        Workout toDelete = null;
        for (Workout workout : workouts) {
            if (id.equals(workout.getId())) {
                toDelete = workout;
                break;
            }
        }
        if (toDelete != null) {
            workouts.remove(toDelete);
        }
    }

    @Override
    public Workout getFirstWorkout() {
        return fakeWorkoutOne();
    }

    private List<Set> createExerciseWithThreeSets(double weight, int reps) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
