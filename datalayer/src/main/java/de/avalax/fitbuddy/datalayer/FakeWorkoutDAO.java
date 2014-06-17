package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.*;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
            setFakedWorkoutId(workout);
        }
    }

    @Override
    public void saveExercise(WorkoutId id, Exercise exercise) {
        setFakedExerciseId(exercise);
    }

    @Override
    public void saveExercise(WorkoutId id, Exercise exercise, int position) {
        setFakedExerciseId(exercise);
    }

    private void setFakedWorkoutId(Workout workout) {
        if (workout.getWorkoutId() == null) {
            UUID uuid = UUID.randomUUID();
            workout.setId(new WorkoutId(uuid.toString()));
        }
    }

    private void setFakedExerciseId(Exercise exercise) {
        if (exercise.getId() == null) {
            long exercise_id = (long) (Math.random() * 10000000);
            exercise.setId(new ExerciseId(exercise_id));
        }
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
            if (workout.getWorkoutId().equals(id)) {
                return workout;
            }
        }
        throw new WorkoutNotAvailableException();
    }

    private Workout fakeWorkoutOne() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(createExercise(1, "Bankdrücken", createThreeSets(70, 12), 5));
        exercises.add(createExercise(2, "Schrägbankdrücken", createThreeSets(40, 10), 5));
        exercises.add(createExercise(3, "Butterfly", createThreeSets(60, 15), 5));
        exercises.add(createExercise(4, "Liegestütze", createThreeSets(0, 30), 12.5));
        exercises.add(createExercise(5, "Lat-Ziehen hinter den Nacken", createThreeSets(50, 12), 5));
        exercises.add(createExercise(6, "Hochziehen der Langhantel mit enger Handstellung", createThreeSets(10, 12), 2.5));
        exercises.add(createExercise(7, "Klimmzüge", createThreeSets(60, 8), 5));
        exercises.add(createExercise(8, "Hammercurls", createThreeSets(30, 20), 2.5));
        exercises.add(createExercise(9, "Larry Scoot Armbeugen", createThreeSets(30, 12), 2.5));
        exercises.add(createExercise(10, "Situps", createThreeSets(0, 30), 2.5));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setName("fake workout one");
        workout.setId(new WorkoutId("1"));
        return workout;
    }

    private BasicExercise createExercise(long id, String name, List<Set> sets, double weightRaise) {
        BasicExercise exercise = new BasicExercise(name, sets, weightRaise);
        exercise.setId(new ExerciseId(id));
        return exercise;
    }

    private Workout fakeWorkoutTwo() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        exercises.add(createExercise(1, "Bankdrücken", createThreeSets(50, 12), 5));
        exercises.add(createExercise(2, "KH-Rudern", createThreeSets(10, 12), 2.5));
        exercises.add(createExercise(3, "KH-Latissimus", createThreeSets(10, 12), 2.5));
        exercises.add(createExercise(4, "Triceps am hohen Block", createThreeSets(25, 12), 5));
        exercises.add(createExercise(5, "Ausfallschritte", createThreeSets(2, 12), 2));
        exercises.add(createExercise(6, "KH-Nackendrücken", createThreeSets(15, 12), 2.5));
        exercises.add(createExercise(7, "Rückenstrecker", createThreeSets(0, 12), 5));
        exercises.add(createExercise(8, "Crunches am Gerät", createThreeSets(45, 12), 5));
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setId(new WorkoutId("2"));
        workout.setName("fake workout two");
        return workout;
    }

    @Override
    public List<Workout> getList() {
        List<Workout> workoutList = new ArrayList<>();

        for (Workout workout : workouts) {
            Workout w = new BasicWorkout(new LinkedList<Exercise>());
            w.setId(workout.getWorkoutId());
            w.setName(workout.getName());
            workoutList.add(w);
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

    @Override
    public Workout getFirstWorkout() {
        return fakeWorkoutOne();
    }

    private List<Set> createThreeSets(double weight, int reps) {
        List<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        sets.add(new BasicSet(weight, reps));
        return sets;
    }
}
