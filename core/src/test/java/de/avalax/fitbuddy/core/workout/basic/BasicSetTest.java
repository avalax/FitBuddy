package de.avalax.fitbuddy.core.workout.basic;


import de.avalax.fitbuddy.core.workout.Set;
import org.junit.Before;
import org.junit.Test;

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
    public void getWeight_shouldGetWeight() throws Exception {
        assertThat(set.getWeight(),equalTo(12.5));
    }

    @Test
    public void getReps_ShouldGetReps() throws Exception {
        set.setReps(MAX_REPS);
        assertThat(set.getReps(), equalTo(MAX_REPS));
    }

	@Test
	public void getMaxReps_shouldGetMaxReps() throws Exception {
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
