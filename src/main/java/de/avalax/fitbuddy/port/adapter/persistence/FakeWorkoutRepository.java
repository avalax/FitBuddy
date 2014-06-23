package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.util.*;

public class FakeWorkoutRepository implements WorkoutRepository {

    private ArrayList<Workout> workouts;

    public FakeWorkoutRepository() {
        workouts = new ArrayList<>();
        workouts.add(fakeWorkoutOne());
        workouts.add(fakeWorkoutTwo());
    }

    @Override
    public void save(Workout workout) {
        if (!workouts.contains(workout)) {
            workouts.add(workout);
            setFakedWorkoutId(workout);
        }
    }

    private void setFakedWorkoutId(Workout workout) {
        if (workout.getWorkoutId() == null) {
            UUID uuid = UUID.randomUUID();
            workout.setWorkoutId(new WorkoutId(uuid.toString()));
        }
    }

    @Override
    public Workout load(WorkoutId id) {
        for (Workout workout : workouts) {
            if (workout.getWorkoutId().equals(id)) {
                return workout;
            }
        }
        throw new WorkoutNotAvailableException();
    }

    private Workout fakeWorkoutOne() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(createExercise("1", "Bankdrücken", createThreeSets(70, 12)));
        exercises.add(createExercise("2", "Schrägbankdrücken", createThreeSets(40, 10)));
        exercises.add(createExercise("3", "Butterfly", createThreeSets(60, 15)));
        exercises.add(createExercise("4", "Liegestütze", createThreeSets(0, 30)));
        exercises.add(createExercise("5", "Lat-Ziehen hinter den Nacken", createThreeSets(50, 12)));
        exercises.add(createExercise("6", "Hochziehen der Langhantel mit enger Handstellung", createThreeSets(10, 12)));
        exercises.add(createExercise("7", "Klimmzüge", createThreeSets(60, 8)));
        exercises.add(createExercise("8", "Hammercurls", createThreeSets(30, 20)));
        exercises.add(createExercise("9", "Larry Scoot Armbeugen", createThreeSets(30, 12)));
        exercises.add(createExercise("10", "Situps", createThreeSets(0, 30)));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setName("fake workout one");
        workout.setWorkoutId(new WorkoutId("1"));
        return workout;
    }

    private BasicExercise createExercise(String id, String name, List<Set> sets) {
        BasicExercise exercise = new BasicExercise(name, sets);
        exercise.setExerciseId(new ExerciseId(id));
        return exercise;
    }

    private Workout fakeWorkoutTwo() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(createExercise("1", "Bankdrücken", createThreeSets(50, 12)));
        exercises.add(createExercise("2", "KH-Rudern", createThreeSets(10, 12)));
        exercises.add(createExercise("3", "KH-Latissimus", createThreeSets(10, 12)));
        exercises.add(createExercise("4", "Triceps am hohen Block", createThreeSets(25, 12)));
        exercises.add(createExercise("5", "Ausfallschritte", createThreeSets(2, 12)));
        exercises.add(createExercise("6", "KH-Nackendrücken", createThreeSets(15, 12)));
        exercises.add(createExercise("7", "Rückenstrecker", createThreeSets(0, 12)));
        exercises.add(createExercise("8", "Crunches am Gerät", createThreeSets(45, 12)));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setWorkoutId(new WorkoutId("2"));
        workout.setName("fake workout two");
        return workout;
    }

    @Override
    public List<WorkoutListEntry> getWorkoutList() {
        List<WorkoutListEntry> workoutList = new ArrayList<>();

        for (Workout workout : workouts) {
            WorkoutListEntry entry = new WorkoutListEntry(workout.getWorkoutId(),workout.getName());
            workoutList.add(entry);
        }
        return workoutList;
    }

    @Override
    public void delete(WorkoutId id) {
        Workout toDelete = null;
        for (Workout workout : workouts) {
            if (id.equals(workout.getWorkoutId())) {
                toDelete = workout;
                break;
            }
        }
        if (toDelete != null) {
            workouts.remove(toDelete);
        }
    }

    private List<Set> createThreeSets(double weight, int reps) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
