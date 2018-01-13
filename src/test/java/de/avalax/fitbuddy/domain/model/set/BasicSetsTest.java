package de.avalax.fitbuddy.domain.model.set;

import org.assertj.core.api.Java6Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
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

    @Test
    public void containsSet_shouldReturnBoolean() throws Exception {
        Set set = aSet().build();
        Sets sets = new BasicSets(new ArrayList<>());
        sets.add(set);

        Java6Assertions.assertThat(sets.contains(null)).isFalse();
        Java6Assertions.assertThat(sets.contains(aSet().build())).isFalse();
        Java6Assertions.assertThat(sets.contains(set)).isTrue();
    }
}