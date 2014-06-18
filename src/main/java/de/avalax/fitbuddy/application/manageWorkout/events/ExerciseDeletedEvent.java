package de.avalax.fitbuddy.application.manageWorkout.events;

import de.avalax.fitbuddy.domain.model.Exercise;

public class ExerciseDeletedEvent {

    public final Exercise exercise;

    public ExerciseDeletedEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
