package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.workout.*;

import javax.inject.Inject;
import java.util.List;

public class WorkoutSession {
    public static final String LAST_WORKOUT_ID = "lastWorkoutId";
    private Workout workout;
    private SharedPreferences sharedPreferences;
    private WorkoutRepository workoutRepository;

    @Inject
    public WorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository, WorkoutFactory workoutFactory) {
        this.sharedPreferences = sharedPreferences;
        this.workoutRepository = workoutRepository;
        String lastWorkoutId = sharedPreferences.getString(LAST_WORKOUT_ID, "1");
        WorkoutId workoutId = new WorkoutId(lastWorkoutId);
        try {
            this.workout = workoutRepository.load(workoutId);
        } catch (WorkoutNotAvailableException wnae) {
            Log.d("WorkoutNotAvailableException", wnae.getMessage(), wnae);
        }
        if (this.workout == null) {
            List<WorkoutListEntry> workoutList = workoutRepository.getWorkoutList();
            if (workoutList.size() > 0) {
                this.workout = workoutRepository.load(workoutList.get(0).getWorkoutId());
            } else {
                this.workout = workoutFactory.createNew();
                workoutRepository.save(this.workout);
            }
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
        editor.putString(LAST_WORKOUT_ID, workoutId.id());
        workout = workoutRepository.load(workoutId);
        editor.commit();
    }
}