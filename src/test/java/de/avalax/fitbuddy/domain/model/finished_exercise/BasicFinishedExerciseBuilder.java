package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder;
import de.avalax.fitbuddy.domain.model.finished_set.BasicFinishedSetBuilder;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;

public class BasicFinishedExerciseBuilder {

    private List<FinishedSet> sets = new ArrayList<>();
    ;

    public static BasicFinishedExerciseBuilder anFinishedExercise() {
        return new BasicFinishedExerciseBuilder();
    }

    public FinishedExercise build() {
        return new BasicFinishedExercise(null, null, null, sets);
    }

    public BasicFinishedExerciseBuilder withFinishedSet(BasicFinishedSetBuilder basicFinishedSet) {
        sets.add(basicFinishedSet.build());
        return this;
    }
}
