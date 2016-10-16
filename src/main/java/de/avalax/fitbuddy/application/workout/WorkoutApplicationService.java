package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class WorkoutApplicationService {
    private WorkoutSession workoutSession;
    private WorkoutRepository workoutRepository;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public WorkoutApplicationService(
            WorkoutSession workoutSession,
            WorkoutRepository workoutRepository,
            FinishedWorkoutRepository finishedWorkoutRepository) {
        this.workoutSession = workoutSession;
        this.workoutRepository = workoutRepository;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    @Deprecated
    public int countOfExercises() throws ResourceException {
        return getWorkout().getExercises().countOfExercises();
    }

    @Deprecated
    public Exercise requestExercise(int position) throws ResourceException {
        return getWorkout().getExercises().exerciseAtPosition(position);
    }

    public void switchToSet(int position, int moved) throws ResourceException {
        Exercise exercise = getWorkout().getExercises().exerciseAtPosition(position);
        exercise.setCurrentSet(exercise.indexOfCurrentSet() + moved);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void addRepsToSet(int position, int moved) throws ResourceException {
        Exercise exercise = getWorkout().getExercises().exerciseAtPosition(position);
        int currentSetIndex = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(currentSetIndex);
        set.setReps(set.getReps() + moved);

        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void setCurrentExercise(int index) throws ResourceException {
        getWorkout().getExercises().setCurrentExercise(index);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void updateWeightOfCurrentSet(int index, double weight) throws ResourceException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        exercise.setAtPosition(indexOfCurrentSet).setWeight(weight);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    @Deprecated
    public double weightOfCurrentSet(int index) throws ResourceException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(indexOfCurrentSet);
        return set.getWeight();
    }

    @Deprecated
    public int indexOfCurrentExercise() throws ResourceException {
        return getWorkout().getExercises().indexOfCurrentExercise();
    }

    @Deprecated
    public WorkoutId currentWorkoutId() throws ResourceException {
        return workoutSession.getWorkout().getWorkoutId();
    }

    public void finishCurrentWorkout() throws ResourceException {
        Workout workout = workoutSession.getWorkout();
        finishedWorkoutRepository.saveWorkout(workout);
        Workout newWorkout = workoutRepository.load(workout.getWorkoutId());
        workoutSession.switchWorkout(newWorkout);
    }

    @Deprecated
    public int workoutProgress(int exerciseIndex) throws ResourceException {
        return progressInPercent(getWorkout().getProgress(exerciseIndex));
    }

    private int progressInPercent(double progress) {
        return (int) Math.round(progress * 100);
    }

    private Workout getWorkout() throws WorkoutException {
        if (workoutSession.hasWorkout()) {
            return workoutSession.getWorkout();
        } else {
            throw new WorkoutException();
        }
    }
}
