package de.avalax.fitbuddy.application.workout;

import android.content.SharedPreferences;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class WorkoutSession {
    public static final String LAST_WORKOUT_ID = "lastWorkoutId";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutRepository workoutRepository;

    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository) {
        this.sharedPreferences = sharedPreferences;
        this.workoutRepository = workoutRepository;
    }

    public void switchToLastLoadedWorkout() throws WorkoutNotFoundException {
        String lastWorkoutId = sharedPreferences.getString(LAST_WORKOUT_ID, "1");
        WorkoutId workoutId = new WorkoutId(lastWorkoutId);
        this.workout = workoutRepository.load(workoutId);
    }

    public void switchWorkoutById(WorkoutId workoutId) {
        try {
            workout = workoutRepository.load(workoutId);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LAST_WORKOUT_ID, workoutId.id());
            editor.commit();
        } catch (WorkoutNotFoundException wnfe) {
            Log.d("WorkoutSession", wnfe.getMessage(), wnfe);
        }
    }

    public Workout getWorkout() {
        return workout;
    }
}