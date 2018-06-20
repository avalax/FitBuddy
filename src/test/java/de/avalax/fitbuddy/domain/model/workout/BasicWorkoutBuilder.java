package de.avalax.fitbuddy.domain.model.workout;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.BasicSetBuilder;
import de.avalax.fitbuddy.domain.model.set.Set;

public class BasicWorkoutBuilder {
    private String name;
    private List<Exercise> exercises = new ArrayList<>();

    public static BasicWorkoutBuilder aWorkout() {
        return new BasicWorkoutBuilder();
    }

    public BasicWorkoutBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Workout build() {
        return new BasicWorkout(null, name, exercises);
    }

    public BasicWorkoutBuilder withExercise(BasicExerciseBuilder basicExerciseBuilder) {
        exercises.add(basicExerciseBuilder.build());
        return this;
    }
}
