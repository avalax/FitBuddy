package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.set.SetException;

import java.util.ArrayList;
import java.util.Collections;
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
    public String getName() {
        return name;
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
    public double getProgress(int position) throws ResourceException {
        Exercise exercise = exerciseAtPosition(position);
        return (position + exercise.getProgress()) / exercises.size();
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    @Override
    public Exercise exerciseAtPosition(int index) throws ExerciseException {
        if (exercises.size() <= index || index < 0) {
            throw new ExerciseException();
        }
        return exercises.get(index);
    }

    @Override
    public Exercise createExercise() {
        return createExercise(exercises.size());
    }

    @Override
    public Exercise createExercise(int position) {
        Exercise exercise = new BasicExercise();
        exercise.createSet();
        exercises.add(position, exercise);
        return exercise;
    }

    @Override
    public void addExercise(int position, Exercise exercise) {
        exercises.add(position, exercise);
    }

    @Override
    public void setCurrentExercise(int index) throws ExerciseException {
        if (exercises.size() <= index || index < 0) {
            throw new ExerciseException();
        }
        exerciseIndex = index;
    }

    @Override
    public int indexOfCurrentExercise() throws ExerciseException {
        if (exercises.isEmpty()) {
            throw new ExerciseException();
        }
        return exerciseIndex;
    }

    @Override
    public boolean moveExerciseAtPositionUp(int index) throws ExerciseException {
        if (index == 0) {
            return false;
        }
        Exercise exercise = exerciseAtPosition(index);
        deleteExercise(exercise);
        addExercise(index - 1, exercise);
        return true;
    }

    @Override
    public boolean moveExerciseAtPositionDown(int index) throws ExerciseException {
        if (index + 1 == countOfExercises()) {
            return false;
        }
        Exercise exercise = exerciseAtPosition(index);
        deleteExercise(exercise);
        addExercise(index + 1, exercise);
        return true;
    }

    @Override
    public int countOfExercises() {
        return exercises.size();
    }

    @Override
    public List<Exercise> exercisesOfWorkout() {
        return Collections.unmodifiableList(exercises);
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
