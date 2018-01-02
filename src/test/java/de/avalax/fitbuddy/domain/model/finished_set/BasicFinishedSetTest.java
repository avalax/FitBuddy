package de.avalax.fitbuddy.domain.model.finished_set;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicFinishedSetTest {
    private FinishedSet set;

    @Before
    public void setUp() throws Exception {
        double weight = 42L;
        int reps = 12;
        int maxReps = 15;
        set = new BasicFinishedSet(weight, reps, maxReps);
    }

    @Test
    public void toString_shouldReturnSetInformation() throws Exception {
        assertThat(set.toString()).isEqualTo("BasicFinishedSet [reps=" + set.getReps() + ", maxReps=" + set.getMaxReps() + ", weight=" + set.getWeight() + "]");
    }
}

