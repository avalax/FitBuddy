package de.avalax.fitbuddy.app.manageWorkout.events;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.ExerciseId;

public class ExerciseDeletedEvent {

    public final Exercise exercise;

    public ExerciseDeletedEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
