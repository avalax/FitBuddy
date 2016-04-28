package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.ResourceNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.io.IOException;

public class WorkoutApplicationService {
    private WorkoutSession workoutSession;
    private WorkoutRepository workoutRepository;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public WorkoutApplicationService(WorkoutSession workoutSession, WorkoutRepository workoutRepository, FinishedWorkoutRepository finishedWorkoutRepository) {
        this.workoutSession = workoutSession;
        this.workoutRepository = workoutRepository;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    public int countOfExercises() throws ResourceNotFoundException {
        return getWorkout().countOfExercises();
    }

    public Exercise requestExercise(int position) throws WorkoutNotFoundException, ExerciseNotFoundException {
        return getWorkout().exerciseAtPosition(position);
    }

    public void switchToSet(int position, int moved) throws ResourceNotFoundException, IOException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        exercise.setCurrentSet(exercise.indexOfCurrentSet() + moved);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void addRepsToSet(int position, int moved) throws ResourceNotFoundException, IOException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        int currentSetIndex = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(currentSetIndex);
        set.setReps(set.getReps() + moved);

        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void setCurrentExercise(int index) throws ResourceNotFoundException, IOException {
        getWorkout().setCurrentExercise(index);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void updateWeightOfCurrentSet(int index, double weight) throws ResourceNotFoundException, IOException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        exercise.setAtPosition(indexOfCurrentSet).setWeight(weight);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public double weightOfCurrentSet(int index) throws ResourceNotFoundException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(indexOfCurrentSet);
        return set.getWeight();
    }

    public int indexOfCurrentExercise() throws ResourceNotFoundException {
        return getWorkout().indexOfCurrentExercise();
    }

    public WorkoutId currentWorkoutId() throws ResourceNotFoundException {
        return workoutSession.getWorkout().getWorkoutId();
    }

    public void finishCurrentWorkout() throws ResourceNotFoundException, IOException {
        Workout workout = workoutSession.getWorkout();
        finishedWorkoutRepository.saveWorkout(workout);
        Workout newWorkout = workoutRepository.load(workout.getWorkoutId());
        workoutSession.switchWorkout(newWorkout);
    }

    public int workoutProgress(int exerciseIndex) throws ResourceNotFoundException {
        return progressInPercent(getWorkout().getProgress(exerciseIndex));
    }

    private int progressInPercent(double progess) {
        return (int) Math.round(progess * 100);
    }

    private Workout getWorkout() throws WorkoutNotFoundException {
        return workoutSession.getWorkout();
    }
}
