package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;

public class WorkoutApplicationService {
    private WorkoutSession workoutSession;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public WorkoutApplicationService(
            WorkoutSession workoutSession,
            FinishedWorkoutRepository finishedWorkoutRepository) {
        this.workoutSession = workoutSession;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    @Deprecated
    public int countOfExercises() throws ResourceException {
        return getWorkout().getExercises().size();
    }

    @Deprecated
    public Exercise requestExercise(int position) throws ResourceException {
        return getWorkout().getExercises().get(position);
    }

    public void switchToSet(int position, int moved) throws ResourceException {
        Exercise exercise = getWorkout().getExercises().get(position);
        exercise.getSets().setCurrentSet(exercise.getSets().indexOfCurrentSet() + moved);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void addRepsToSet(int position, int moved) throws ResourceException {
        Exercise exercise = getWorkout().getExercises().get(position);
        int currentSetIndex = exercise.getSets().indexOfCurrentSet();
        Set set = exercise.getSets().get(currentSetIndex);
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
        int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
        exercise.getSets().get(indexOfCurrentSet).setWeight(weight);
        //TODO only saveWorkout by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    @Deprecated
    public double weightOfCurrentSet(int index) throws ResourceException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
        Set set = exercise.getSets().get(indexOfCurrentSet);
        return set.getWeight();
    }

    @Deprecated
    public int indexOfCurrentExercise() throws ResourceException {
        return getWorkout().getExercises().indexOfCurrentExercise();
    }

    public void finishCurrentWorkout() throws ResourceException {
        if (workoutSession.hasWorkout()) {
            Workout workout = workoutSession.getWorkout();
            finishedWorkoutRepository.saveWorkout(workout);
            workoutSession.switchWorkout(null);
        }
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
