package de.avalax.fitbuddy.application.manageWorkout.events;

import de.avalax.fitbuddy.domain.model.Exercise;

public class ExerciseChangedEvent {

    public final Exercise exercise;

    public ExerciseChangedEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
