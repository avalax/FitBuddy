package de.avalax.fitbuddy.app.manageWorkout.events;

import de.avalax.fitbuddy.core.workout.Exercise;

public class ExerciseChangedEvent {

    public final Exercise exercise;

    public ExerciseChangedEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
