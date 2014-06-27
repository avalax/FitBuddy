package de.avalax.fitbuddy.application;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

@Deprecated
public class ExerciseFactory {
    public Exercise createNew() {
        return new BasicExercise();
    }
}
