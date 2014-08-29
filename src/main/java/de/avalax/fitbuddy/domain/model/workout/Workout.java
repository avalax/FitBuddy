package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;

import java.io.Serializable;

public interface Workout extends Serializable {
    WorkoutId getWorkoutId();

    void setWorkoutId(WorkoutId workoutId);

    void setName(String name);

    String getName();

    double getProgress(int index) throws ExerciseNotFoundException, SetNotFoundException;

    Exercise createExercise();

    Exercise createExercise(int index);

    void addExercise(int index, Exercise exercise);

    void replaceExercise(Exercise exercise);

    void deleteExercise(Exercise exercise);

    Exercise exerciseAtPosition(int index) throws ExerciseNotFoundException;

    void setCurrentExercise(int index);

    int indexOfCurrentExercise() throws ExerciseNotFoundException;

    int countOfExercises();
}