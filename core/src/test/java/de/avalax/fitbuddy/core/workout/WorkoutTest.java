package de.avalax.fitbuddy.core.workout;

import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class WorkoutTest {

    @Test
    public void Workout_ShouldBeSerializable() throws Exception {
        Workout workout = mock(Workout.class);
        assertThat(workout, instanceOf(Serializable.class));
    }
}
