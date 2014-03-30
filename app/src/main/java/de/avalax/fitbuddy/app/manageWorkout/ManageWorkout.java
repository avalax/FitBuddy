package de.avalax.fitbuddy.app.manageWorkout;

import de.avalax.fitbuddy.core.workout.Workout;

public class ManageWorkout {

    private boolean unsavedChanges;

    private Integer exercisePosition;

    private Workout workout;

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public boolean hasUnsavedChanges() {
        return unsavedChanges;
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


}
