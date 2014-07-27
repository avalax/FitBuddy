package de.avalax.fitbuddy.application.workout;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public class WorkoutSession {
    private SharedPreferences sharedPreferences;

    public WorkoutSession(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void switchWorkoutById(WorkoutId workoutId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, workoutId.id());
        editor.commit();
    }
}