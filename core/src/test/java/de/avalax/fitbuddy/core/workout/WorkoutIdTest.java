package de.avalax.fitbuddy.core.workout;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class WorkoutIdTest {

    @Test
    public void testSameIdentity() throws Exception {
        assertThat(new WorkoutId(42),equalTo(new WorkoutId(42)));
        assertThat(new WorkoutId(42).hashCode(),equalTo(new WorkoutId(42).hashCode()));
    }
}