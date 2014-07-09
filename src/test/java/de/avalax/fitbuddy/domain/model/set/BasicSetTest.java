package de.avalax.fitbuddy.domain.model.set;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicSetTest {
    public static final int MAX_REPS = 15;
    private static final double WEIGHT = 12.5;
    Set set;

    @Before
    public void setUp() throws Exception {
        set = new BasicSet(WEIGHT, MAX_REPS);
    }

    @Test
    public void equalSetId_shouldHaveSameIdentity() throws Exception {
        set.setSetId(new SetId("42"));
        Set set2 = new BasicSet(WEIGHT, MAX_REPS);
        set2.setSetId(new SetId("42"));

        assertThat(set, equalTo(set2));
        assertThat(set.hashCode(), equalTo(set2.hashCode()));
    }

    @Test
    public void differntSetIds_shouldHaveDifferentIdentity() throws Exception {
        set.setSetId(new SetId("21"));
        Set set2 = new BasicSet(WEIGHT, MAX_REPS);
        set2.setSetId(new SetId("42"));

        assertThat(set, not(equalTo(set2)));
        assertThat(set.hashCode(), not(equalTo(set2.hashCode())));
    }

    @Test
    public void nullValue_shouldHaveDifferentIdentity() throws Exception {
        Set set2 = new BasicSet(WEIGHT, MAX_REPS);

        assertThat(set, not(equalTo(set2)));
        assertThat(set.hashCode(), not(equalTo(set2.hashCode())));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        SetId setId = new SetId("42");

        set.setSetId(setId);

        assertThat(set.getSetId(), equalTo(setId));
    }

    @Test
    public void getWeight_shouldGetWeight() throws Exception {
        assertThat(set.getWeight(), equalTo(WEIGHT));
    }

    @Test
    public void setWeight_shouldSetWeight() throws Exception {
        set.setWeight(42.21);

        assertThat(set.getWeight(), equalTo(42.21));
    }

    @Test
    public void setReps_shouldSetReps() throws Exception {
        set.setReps(MAX_REPS);

        assertThat(set.getReps(), equalTo(MAX_REPS));
    }

    @Test
    public void setMaxRepsOnInit_shouldSetMaxReps() throws Exception {
        assertThat(set.getMaxReps(), equalTo(MAX_REPS));
    }

    @Test
    public void setMaxReps_shouldSetMaxReps() throws Exception {
        set.setMaxReps(42);

        assertThat(set.getMaxReps(), equalTo(42));
    }

    @Test
    public void setReps_shouldShouldSetToZeroReps() throws Exception {
        set.setReps(-1);

        assertThat(set.getReps(), equalTo(0));
    }

    @Test
    public void setReps_shouldSetToMaxRepsWhenGreaterThenMaxReps() throws Exception {
        set.setReps(MAX_REPS + 1);

        assertThat(set.getReps(), equalTo(MAX_REPS));
    }

    @Test
    public void toString_shouldReturnSetInformations() throws Exception {
        assertThat(set.toString(), equalTo("BasicSet [weight=" + WEIGHT + ", maxReps="+MAX_REPS+"]"));
        SetId setId = new SetId("42");
        set.setSetId(setId);
        assertThat(set.toString(), equalTo("BasicSet [weight=" + WEIGHT + ", maxReps="+MAX_REPS+", setId=" + setId.toString() + "]"));
    }
}
