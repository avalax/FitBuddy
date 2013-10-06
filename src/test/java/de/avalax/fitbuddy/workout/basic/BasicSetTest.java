package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.exceptions.RepsExceededException;
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
    public void BasicSet_ShouldGetWeight() throws Exception {
        assertThat(set.getWeight(),equalTo(12.5));
    }

    @Test
    public void BasicSet_ShouldGetReps() throws Exception {
        set.setReps(15);
        assertThat(set.getReps(), equalTo(15));
    }

	@Test
	public void BasicSet_ShouldGetMaxReps() throws Exception {
		assertThat(set.getMaxReps(),equalTo(15));
	}

    @Test(expected = RepsExceededException.class)
    public void BasicSet_ShouldThrowExceptionOnRepsGreaterThenMaxReps() throws Exception {
        set.setReps(16);
    }
}
