package de.avalax.fitbuddy.app.manageWorkout.events;

import de.avalax.fitbuddy.core.workout.Exercise;

public class ExerciseChangedEvent {

    public final int position;

    public final Exercise exercise;

    public ExerciseChangedEvent(int position, Exercise exercise) {
        this.position = position;
        this.exercise = exercise;
    }
}
