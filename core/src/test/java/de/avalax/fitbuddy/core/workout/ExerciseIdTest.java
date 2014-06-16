package de.avalax.fitbuddy.core.workout;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExerciseIdTest {

    @Test
    public void testSameIdentity() throws Exception {
        assertThat(new ExerciseId(42),equalTo(new ExerciseId(42)));
        assertThat(new ExerciseId(42).hashCode(),equalTo(new ExerciseId(42).hashCode()));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new ExerciseId(42)).toString(),equalTo("42"));
        assertThat(new ExerciseId(21).toString(),equalTo("21"));
    }
}