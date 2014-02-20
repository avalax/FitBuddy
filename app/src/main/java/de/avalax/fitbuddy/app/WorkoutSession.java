package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Inject;

public class WorkoutSession {
    protected static final String LAST_WORKOUT_POSITION = "lastWorkoutPosition";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutDAO workoutDAO;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutDAO workoutDAO) {
        this.sharedPreferences = sharedPreferences;
        this.workoutDAO = workoutDAO;
        int workoutPosition = sharedPreferences.getInt(LAST_WORKOUT_POSITION, 0);
        this.workout = workoutDAO.load(workoutPosition);
    }

    public void saveWorkout() {
        workoutDAO.save(workout);
    }

    public Workout getWorkout() {
        return workout;
    }

    public void switchWorkout(int position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LAST_WORKOUT_POSITION, position);
        workout = workoutDAO.load(position);
        editor.commit();
    }
}