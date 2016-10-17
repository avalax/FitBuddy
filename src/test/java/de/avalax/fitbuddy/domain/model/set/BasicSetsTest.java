package de.avalax.fitbuddy.domain.model.set;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

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

        assertThat(sets.toString(), equalTo("BasicSets [sets=" +
                "[BasicSet [weight=" + weight + ", maxReps=" + maxReps + ", setId=" + setId.toString() + "]]]"));
    }
}