package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Set;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicSetTest {
    Set set;

    @Before
    public void setUp() throws Exception {
        set = new BasicSet(12.5, 15);
    }

    @Test
    public void getWeight_shouldGetWeight() throws Exception {
        assertThat(set.getWeight(),equalTo(12.5));
    }

    @Test
    public void getReps_ShouldGetReps() throws Exception {
        set.setReps(15);
        assertThat(set.getReps(), equalTo(15));
    }

	@Test
	public void getRepsSize_shouldGetMaxReps() throws Exception {
		assertThat(set.getRepsSize(),equalTo(15));
	}

    @Test
    public void setReps_shouldShouldSetToMaxReps() throws Exception {
        set.setReps(-1);

        assertThat(set.getReps(),equalTo(set.getRepsSize()));
    }

    @Test
    public void setReps_shouldAllowRepsGreaterThenMaxReps() throws Exception {
        set.setReps(16);

        assertThat(set.getReps(),equalTo(0));
    }
}
