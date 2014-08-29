package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class BasicWorkout implements Workout {
    private List<Exercise> exercises;
    private String name;
    private WorkoutId workoutId;
    private int exerciseIndex;

    public BasicWorkout(WorkoutId workoutId, String name, List<Exercise> exercises) {
        this.workoutId = workoutId;
        this.exercises = exercises;
        this.name = name;
    }

    public BasicWorkout() {
        this.exercises = new ArrayList<>();
        this.name = "";
    }

    @Override
    public WorkoutId getWorkoutId() {
        return workoutId;
    }

    @Override
    public void setWorkoutId(WorkoutId workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public void setName(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name.trim();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getProgress(int index) throws ExerciseNotFoundException, SetNotFoundException {
        Exercise exercise = exerciseAtPosition(index);
        return (index + exercise.getProgress()) / exercises.size();
    }

    @Override
    public void replaceExercise(Exercise exercise) {
        int indexOf = exercises.indexOf(exercise);
        if (indexOf >= 0) {
            exercises.set(indexOf, exercise);
        }
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    @Override
    public Exercise exerciseAtPosition(int index) throws ExerciseNotFoundException {
        if (exercises.size() <= index) {
            throw new ExerciseNotFoundException();
        }
        return exercises.get(index);
    }

    @Override
    public Exercise createExercise() {
        Exercise exercise = new BasicExercise();
        exercises.add(exercise);
        return exercise;
    }

    @Override
    public Exercise createExercise(int index) {
        Exercise exercise = new BasicExercise();
        exercises.add(index, exercise);
        return exercise;
    }

    @Override
    public void addExercise(int index, Exercise exercise) {
        exercises.add(index, exercise);
    }

    @Override
    public void setCurrentExercise(int index) {
        exerciseIndex = index;
    }

    @Override
    public int indexOfCurrentExercise() throws ExerciseNotFoundException {
        if (exercises.isEmpty()) {
            throw new ExerciseNotFoundException();
        }
        return exerciseIndex;
    }

    @Override
    public int countOfExercises() {
        return exercises.size();
    }

    @Override
    public String toString() {
        if (workoutId == null) {
            return "BasicWorkout [name=" + name + "]";
        }
        return "BasicWorkout [name=" + name + ", workoutId=" + workoutId.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (workoutId == null) {
            return super.equals(o);
        }
        return o instanceof Workout && workoutId.equals(((Workout) o).getWorkoutId());
    }

    @Override
    public int hashCode() {
        if (workoutId == null) {
            return super.hashCode();
        }
        return workoutId.hashCode();
    }
}
