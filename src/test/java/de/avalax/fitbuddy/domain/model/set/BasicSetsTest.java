package de.avalax.fitbuddy.domain.model.set;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercises;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BasicSetsTest {
    @Test
    public void toString_shouldReturnSetsInformation() throws Exception {
        Set set = new BasicSet();
        double weight = 12.5;
        int maxReps = 15;
        set.setMaxReps(maxReps);
        set.setWeight(12.5);
        SetId setId = new SetId("42");
        set.setSetId(setId);

        Sets sets = new BasicSets(Collections.singletonList(set));

        assertThat(sets.toString()).isEqualTo("BasicSets [sets=" +
                "[BasicSet [weight=" + weight + ", maxReps=" + maxReps + ", setId=" + setId.toString() + "]]]");
    }

    @Test
    public void addSet_shouldBeAddedToSet() throws Exception {
        Set set = aSet().build();
        Sets sets = new BasicSets(new ArrayList<>());

        sets.add(set);

        assertThat(sets).containsOnly(set);
    }

    @Test
    public void setSet_shouldReplaceSetAtPosition() throws Exception {
        Set set = aSet().build();
        Set newSet = aSet().build();
        Sets sets = new BasicSets(new ArrayList<>());
        sets.add(set);

        sets.set(0, newSet);

        assertThat(sets).containsOnly(newSet);
    }
}