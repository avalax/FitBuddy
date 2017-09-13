package de.avalax.fitbuddy.domain.model.exercise;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.set.BasicSetBuilder;
import de.avalax.fitbuddy.domain.model.set.Set;

public class BasicExerciseBuilder {
    private String name;
    private List<Set> sets = new ArrayList<>();

    public static BasicExerciseBuilder anExercise() {
        return new BasicExerciseBuilder();
    }

    public BasicExerciseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Exercise build() {
        return new BasicExercise(null, name, sets);
    }

    public BasicExerciseBuilder withSet(BasicSetBuilder basicSetBuilder) {
        sets.add(basicSetBuilder.build());
        return this;
    }
}
