package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Workout;

public class ManageWorkout {

    protected WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Integer exercisePosition;

    private Workout workout;

    public ManageWorkout(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
    }

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public int unsavedChangesVisibility() {
        return unsavedChanges ? View.VISIBLE : View.GONE;
    }

    public void setExercisePosition(Integer exercisePosition) {
        this.exercisePosition = exercisePosition;
    }

    public Integer getExercisePosition() {
        return exercisePosition;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        unsavedChanges = false;
        this.workout = workout;
    }

    public void switchWorkout(int position) {
        workoutSession.switchWorkout(position);
    }
}
