package de.avalax.fitbuddy.domain.model;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class WorkoutIdTest {

    @Test
    public void testSameIdentity() throws Exception {
        assertThat(new WorkoutId("42"),equalTo(new WorkoutId("42")));
        assertThat(new WorkoutId(new WorkoutId("42")),equalTo(new WorkoutId("42")));
        assertThat(new WorkoutId("42").hashCode(),equalTo(new WorkoutId("42").hashCode()));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new WorkoutId("42")).toString(),equalTo("WorkoutId [id=42]"));
        assertThat(new WorkoutId("21").toString(),equalTo("WorkoutId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new WorkoutId("42")).id(),equalTo("42"));
        assertThat(new WorkoutId("21").id(),equalTo("21"));
    }
}