package de.avalax.fitbuddy.application.workout;

import android.util.Log;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;

public class WorkoutApplicationService {
    private WorkoutSession workoutSession;

    public WorkoutApplicationService(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
    }

    public int countOfCurrentExercises() throws WorkoutNotFoundException {
        return getWorkout().countOfExercises();
    }

    public Exercise requestExercise(int position) throws WorkoutNotFoundException, ExerciseNotFoundException {
        return getWorkout().exerciseAtPosition(position);
    }

    public void switchToSet(int position, int moved) throws WorkoutNotFoundException, ExerciseNotFoundException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        exercise.setCurrentSet(exercise.indexOfCurrentSet() + moved);
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void addRepsToSet(int position, int moved) throws WorkoutNotFoundException, ExerciseNotFoundException {
        Exercise exercise = getWorkout().exerciseAtPosition(position);
        int currentSetIndex = exercise.indexOfCurrentSet();
        try {
            Set set = exercise.setAtPosition(currentSetIndex);
            set.setReps(set.getReps() + moved);
        } catch (SetNotAvailableException e) {
            Log.d("can't update reps", e.getMessage(), e);
        }
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public void setSelectedExerciseIndex(int index) throws WorkoutNotFoundException {
        getWorkout().setCurrentExercise(index);
        //TODO only save by android lifecycle
        workoutSession.saveCurrentWorkout();
    }

    public int indexOfCurrentExercise() throws WorkoutNotFoundException {
        return getWorkout().indexOfCurrentExercise();
    }

    public int workoutProgress(int exerciseIndex) throws WorkoutNotFoundException {
        return calculateProgressbarHeight(getWorkout().getProgress(exerciseIndex));
    }

    private int calculateProgressbarHeight(double progess) {
        return (int) Math.round(progess * 100);
    }

    public Workout getWorkout() throws WorkoutNotFoundException {
        return workoutSession.getWorkout();
    }
}
