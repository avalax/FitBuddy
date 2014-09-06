package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;

public class WorkoutApplicationService {
    private WorkoutSession workoutSession;

    public WorkoutApplicationService(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
    }

    public int countOfExercises() throws RessourceNotFoundException {
        return getWorkout().countOfExercises();
    }

    public Exercise requestExercise(int position) throws WorkoutNotFoundException, ExerciseNotFoundException {
        return getWorkout().exerciseAtPosition(position);
    }

    public void switchToSet(int position, int moved) throws RessourceNotFoundException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        exercise.setCurrentSet(exercise.indexOfCurrentSet() + moved);
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void addRepsToSet(int position, int moved) throws RessourceNotFoundException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        int currentSetIndex = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(currentSetIndex);
        set.setReps(set.getReps() + moved);

        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void setCurrentExercise(int index) throws RessourceNotFoundException {
        getWorkout().setCurrentExercise(index);
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void updateWeightOfCurrentSet(int index, double weight) throws RessourceNotFoundException {
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        exercise.setAtPosition(indexOfCurrentSet).setWeight(weight);
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public double weightOfCurrentSet(int index) throws RessourceNotFoundException{
        Exercise exercise = requestExercise(index);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(indexOfCurrentSet);
        return set.getWeight();
    }

    public int indexOfCurrentExercise() throws RessourceNotFoundException {
        return getWorkout().indexOfCurrentExercise();
    }

    public WorkoutId currentWorkoutId() throws RessourceNotFoundException {
        return workoutSession.getWorkout().getWorkoutId();
    }

    public int workoutProgress(int exerciseIndex) throws RessourceNotFoundException {
        return progressInPercent(getWorkout().getProgress(exerciseIndex));
    }

    private int progressInPercent(double progess) {
        return (int) Math.round(progess * 100);
    }

    private Workout getWorkout() throws WorkoutNotFoundException {
        return workoutSession.getWorkout();
    }
}
