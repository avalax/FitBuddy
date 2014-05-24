package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import android.util.Log;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;
import de.avalax.fitbuddy.datalayer.WorkoutNotAvailableException;

import javax.inject.Inject;

public class WorkoutSession {
    public static final String LAST_WORKOUT_POSITION = "lastWorkoutPosition";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutDAO workoutDAO;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutDAO workoutDAO) {
        this.sharedPreferences = sharedPreferences;
        this.workoutDAO = workoutDAO;
        WorkoutId workoutId = new WorkoutId(sharedPreferences.getLong(LAST_WORKOUT_POSITION, 1L));
        try {
            this.workout = workoutDAO.load(workoutId);
        } catch (WorkoutNotAvailableException wnae) {
            Log.d("WorkoutNotAvailableException", wnae.getMessage(), wnae);
        }
        if (this.workout == null) {
            this.workout = workoutDAO.getFirstWorkout();
            switchWorkout(this.workout.getId());
        }
    }

    public void saveWorkoutSession() {
        //TODO: save workout session
    }

    public Workout getWorkout() {
        return workout;
    }

    public void switchWorkout(WorkoutId id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_WORKOUT_POSITION, id.getId());
        workout = workoutDAO.load(id);
        editor.commit();
    }
}