package de.avalax.fitbuddy.app;

import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Inject;

public class WorkoutSession {
    private Workout workout;
    private WorkoutDAO workoutDAO;

    @Inject
    public WorkoutSession(WorkoutDAO workoutDAO, int lastPosition) {
        this.workoutDAO = workoutDAO;
        this.workout = workoutDAO.load(lastPosition);
    }

    public void saveWorkout() {
        workoutDAO.save(workout);
    }

    public Workout getWorkout() {
        return workout;
    }

    public String[] getWorkoutlist() {
        return workoutDAO.getWorkoutlist();
    }

    public void switchWorkout(int position) {
        workout = workoutDAO.load(position);
    }
}
