package de.avalax.fitbuddy.domain.model.workout;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

public class BasicWorkoutBuilder {
    private String name = "anyWorkout";
    private List<Exercise> exercises = new ArrayList<>();

    public static BasicWorkoutBuilder aWorkout() {
        return new BasicWorkoutBuilder();
    }

    public Workout build() {
        Workout workout = new BasicWorkout();
        workout.setName(name);
        for (Exercise exercise : exercises) {
            workout.getExercises().add(exercise);
        }
        return workout;
    }

    public BasicWorkoutBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BasicWorkoutBuilder withExercise(BasicExerciseBuilder builder) {
        exercises.add(builder.build());
        return this;
    }
}
