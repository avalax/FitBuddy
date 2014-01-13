package de.avalax.fitbuddy.app;

import com.google.inject.Inject;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

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
