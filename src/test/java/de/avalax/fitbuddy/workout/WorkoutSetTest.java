package de.avalax.fitbuddy.workout;


import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class WorkoutSetTest {

    @Test
    public void WorkoutSet_ShouldBeSerializable() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        assertThat(workoutSet,instanceOf(Serializable.class));
    }
}
