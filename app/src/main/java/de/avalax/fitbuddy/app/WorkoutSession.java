package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Inject;

public class WorkoutSession {
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutDAO workoutDAO;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutDAO workoutDAO) {
        this.sharedPreferences = sharedPreferences;
        this.workoutDAO = workoutDAO;
        int workoutPosition = sharedPreferences.getInt("lastWorkoutPosition", 0);
        this.workout = workoutDAO.load(workoutPosition);
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
