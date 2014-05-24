package de.avalax.fitbuddy.core.workout.basic;


import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.ExerciseId;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.SetId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicSetTest {
    public static final int MAX_REPS = 15;
    Set set;

    @Before
    public void setUp() throws Exception {
        set = new BasicSet(12.5, MAX_REPS);
    }

    @Test
    public void testSameIdentity() throws Exception {
        Set a1 = new BasicSet(12.5, MAX_REPS);
        a1.setId(new SetId(42));
        Set a2 = new BasicSet(12.5, MAX_REPS);
        a2.setId(new SetId(42));
        Assert.assertThat(a1, equalTo(a2));
        Assert.assertThat(a1.hashCode(), equalTo(a2.hashCode()));
    }

    @Test
    public void testDifferentIdentity() throws Exception {
        Set a1 = new BasicSet(12.5, MAX_REPS);
        a1.setId(new SetId(21));
        Set a2 = new BasicSet(12.5, MAX_REPS);
        a2.setId(new SetId(42));
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void testDifferentIdentityWithNoId() throws Exception {
        Set a1 = new BasicSet(12.5, MAX_REPS);
        Set a2 = new BasicSet(12.5, MAX_REPS);
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
         public void getId_shouldReturnId() throws Exception {
        SetId id = new SetId(42);

        set.setId(id);

        assertThat(set.getId(), equalTo(id));
    }

    @Test
    public void getWeight_shouldGetWeight() throws Exception {
        assertThat(set.getWeight(),equalTo(12.5));
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
	public void setMaxReps_shouldSetMaxReps() throws Exception {
		assertThat(set.getMaxReps(),equalTo(MAX_REPS));
	}

    @Test
    public void setReps_shouldShouldSetToZeroReps() throws Exception {
        set.setReps(-1);

        assertThat(set.getReps(),equalTo(0));
    }

    @Test
    public void setReps_shouldSetToMaxRepsWhenGreaterThenMaxReps() throws Exception {
        set.setReps(MAX_REPS + 1);

        assertThat(set.getReps(),equalTo(MAX_REPS));
    }
}
