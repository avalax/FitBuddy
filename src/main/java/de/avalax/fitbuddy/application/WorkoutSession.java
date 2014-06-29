package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import javax.inject.Inject;

public class WorkoutSession {
    public static final String LAST_WORKOUT_ID = "lastWorkoutId";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutRepository workoutRepository;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository) {
        this.sharedPreferences = sharedPreferences;
        this.workoutRepository = workoutRepository;
    }

    public void switchToLastLoadedWorkout() {
        String lastWorkoutId = sharedPreferences.getString(LAST_WORKOUT_ID, "1");
        WorkoutId workoutId = new WorkoutId(lastWorkoutId);
        this.workout = workoutRepository.load(workoutId);
    }

    public void switchWorkoutById(WorkoutId workoutId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_WORKOUT_ID, workoutId.id());
        workout = workoutRepository.load(workoutId);
        editor.commit();
    }

    public void saveWorkoutSession() {
        //TODO: save workout session
    }

    public Workout getWorkout() {
        return workout;
    }
}