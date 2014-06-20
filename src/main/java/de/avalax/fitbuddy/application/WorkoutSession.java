package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotAvailableException;

import javax.inject.Inject;

public class WorkoutSession {
    public static final String LAST_WORKOUT_POSITION = "lastWorkoutPosition";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutRepository workoutRepository;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository) {
        this.sharedPreferences = sharedPreferences;
        this.workoutRepository = workoutRepository;
        String lastWorkoutId = sharedPreferences.getString(LAST_WORKOUT_POSITION, "1");
        WorkoutId workoutId = new WorkoutId(lastWorkoutId);
        try {
            this.workout = workoutRepository.load(workoutId);
        } catch (WorkoutNotAvailableException wnae) {
            Log.d("WorkoutNotAvailableException", wnae.getMessage(), wnae);
        }
        if (this.workout == null) {
            this.workout = workoutRepository.getFirstWorkout();
            switchWorkout(this.workout.getWorkoutId());
        }
    }

    public void saveWorkoutSession() {
        //TODO: save workout session
    }

    public Workout getWorkout() {
        return workout;
    }

    public void switchWorkout(WorkoutId workoutId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_WORKOUT_POSITION, workoutId.id());
        workout = workoutRepository.load(workoutId);
        editor.commit();
    }
}