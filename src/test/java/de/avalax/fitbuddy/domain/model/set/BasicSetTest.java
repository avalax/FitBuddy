package de.avalax.fitbuddy.domain.model.set;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicSetTest {
    private Set set;

    @Before
    public void setUp() throws Exception {
        set = new BasicSet();
    }

    @Test
    public void equalSetId_shouldHaveSameIdentity() throws Exception {
        set.setSetId(new SetId("42"));
        Set set2 = new BasicSet();
        set2.setSetId(new SetId("42"));

        assertThat(set, equalTo(set2));
        assertThat(set.hashCode(), equalTo(set2.hashCode()));
    }

    @Test
    public void differntSetIds_shouldHaveDifferentIdentity() throws Exception {
        set.setSetId(new SetId("21"));
        Set set2 = new BasicSet();
        set2.setSetId(new SetId("42"));

        assertThat(set, not(equalTo(set2)));
        assertThat(set.hashCode(), not(equalTo(set2.hashCode())));
    }

    @Test
    public void nullValue_shouldHaveDifferentIdentity() throws Exception {
        Set set2 = new BasicSet();

        assertThat(set, not(equalTo(set2)));
        assertThat(set.hashCode(), not(equalTo(set2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        set.setSetId(new SetId("42"));
        assertThat(set.equals("42"), is(false));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        SetId setId = new SetId("42");

        set.setSetId(setId);

        assertThat(set.getSetId(), equalTo(setId));
    }

    @Test
    public void getWeight_shouldGetWeight() throws Exception {
        set.setWeight(12.5);

        assertThat(set.getWeight(), equalTo(12.5));
    }

    @Test
    public void setWeight_shouldSetWeight() throws Exception {
        set.setWeight(42.21);

        assertThat(set.getWeight(), equalTo(42.21));
    }

    @Test
    public void setReps_shouldSetReps() throws Exception {
        set.setMaxReps(15);
        set.setReps(15);

        assertThat(set.getReps(), equalTo(15));
    }

    @Test
    public void setMaxRepsOnInit_shouldSetMaxReps() throws Exception {
        set.setMaxReps(15);

        assertThat(set.getMaxReps(), equalTo(15));
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
        set.setMaxReps(15);
        set.setReps(15 + 1);

        assertThat(set.getReps(), equalTo(15));
    }

    @Test
    public void getProgress_shouldReturnZeroProgress() throws Exception {
        set.setMaxReps(100);
        set.setReps(0);

        assertThat(set.getProgress(), equalTo(0.0));
    }

    @Test
         public void getProgress_shouldReturnFullProgress() throws Exception {
        set.setMaxReps(100);
        set.setReps(100);

        assertThat(set.getProgress(), equalTo(1.0));
    }

    @Test
    public void getProgress_shouldReturnHalfProgress() throws Exception {
        set.setMaxReps(100);
        set.setReps(50);

        assertThat(set.getProgress(), equalTo(0.5));
    }

    @Test
    public void toString_shouldReturnSetInformationsFromConstruction() throws Exception {
        assertThat(set.toString(), equalTo("BasicSet [weight=0.0, maxReps=1]"));
    }

    @Test
    public void toString_shouldReturnSetInformations() throws Exception {
        double weight = 12.5;
        int maxReps = 15;
        set.setMaxReps(maxReps);
        set.setWeight(12.5);
        SetId setId = new SetId("42");
        set.setSetId(setId);

        assertThat(set.toString(), equalTo("BasicSet [weight=" + weight + ", maxReps=" + maxReps + ", setId=" + setId.toString() + "]"));
    }
}
