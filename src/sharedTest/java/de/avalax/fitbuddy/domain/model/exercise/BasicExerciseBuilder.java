package de.avalax.fitbuddy.domain.model.exercise;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.set.BasicSetBuilder;
import de.avalax.fitbuddy.domain.model.set.Set;

public class BasicExerciseBuilder {
    private String name = "anyExercise";
    private List<Set> sets = new ArrayList<>();

    public static BasicExerciseBuilder anExercise() {
        return new BasicExerciseBuilder();
    }

    public Exercise build() {
        Exercise exercise = new BasicExercise();
        exercise.setName(name);
        for (Set set : sets) {
            exercise.getSets().add(set);
        }
        return exercise;
    }

    public BasicExerciseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BasicExerciseBuilder withSet(BasicSetBuilder builder) {
        sets.add(builder.build());
        return this;
    }
}
