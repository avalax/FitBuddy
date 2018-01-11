package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;

public class WorkoutService {
    private WorkoutSession workoutSession;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public WorkoutService(
            WorkoutSession workoutSession,
            FinishedWorkoutRepository finishedWorkoutRepository) {
        this.workoutSession = workoutSession;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
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

    @Deprecated
    public int indexOfCurrentExercise() throws ResourceException {
        return getWorkout().getExercises().indexOfCurrentExercise();
    }

    @Deprecated
    public FinishedWorkoutId switchWorkout(Workout workout) throws WorkoutException {
        FinishedWorkoutId finishedWorkoutId = null;
        if (workoutSession.hasWorkout()) {
            Workout workoutToSave = workoutSession.getWorkout();
            finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workoutToSave);
        }
        workoutSession.switchWorkout(workout);
        return finishedWorkoutId;
    }

    @Deprecated
    public FinishedWorkoutId finishCurrentWorkout() throws ResourceException {
        return switchWorkout(null);
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

    public boolean hasPreviousExercise(int exerciseIndex) {
        if (!workoutSession.hasWorkout()) {
            return false;
        }
        int size = workoutSession.getWorkout().getExercises().size();
        return exerciseIndex > 0 && exerciseIndex < size;
    }

    public boolean hasNextExercise(int exerciseIndex) {
        if (!workoutSession.hasWorkout()) {
            return false;
        }
        int size = workoutSession.getWorkout().getExercises().size();
        return size > exerciseIndex + 1;
    }

    public boolean hasActiveWorkout() {
        return workoutSession.hasWorkout();
    }

    public boolean isActiveWorkout(Workout workout) {
        return workoutSession.hasWorkout()
                && workoutSession.getWorkout().equals(workout);
    }
}
