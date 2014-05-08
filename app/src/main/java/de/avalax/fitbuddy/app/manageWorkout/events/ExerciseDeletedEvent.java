package de.avalax.fitbuddy.app.manageWorkout.events;

import de.avalax.fitbuddy.core.workout.Exercise;

public class ExerciseDeletedEvent {

    public final int position;

    public final Exercise exercise;

    public ExerciseDeletedEvent(int position, Exercise exercise) {
        this.position = position;
        this.exercise = exercise;
    }
}
