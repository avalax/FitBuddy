package de.avalax.fitbuddy.app;

import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Inject;

public class WorkoutSession {
    private Workout workout;
    private WorkoutDAO workoutDAO;

    @Inject
    public WorkoutSession(WorkoutDAO workoutDAO) {
        this.workoutDAO = workoutDAO;
        this.workout = workoutDAO.load();
    }

    public void finishWorkout() {
        workoutDAO.save(workout);
        workout = workoutDAO.load();
    }

    public Workout getWorkout() {
        return workout;
    }
}
