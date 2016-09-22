package de.avalax.fitbuddy.domain.model.workout;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class WorkoutIdTest {

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testSameIdentity() throws Exception {
        assertThat(new WorkoutId("42"), equalTo(new WorkoutId("42")));
        assertThat(new WorkoutId("42").hashCode(), equalTo(new WorkoutId("42").hashCode()));
        assertThat(new WorkoutId("42").equals("42"), is(false));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new WorkoutId("42")).toString(), equalTo("WorkoutId [id=42]"));
        assertThat(new WorkoutId("21").toString(), equalTo("WorkoutId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new WorkoutId("42")).getId(), equalTo("42"));
        assertThat(new WorkoutId("21").getId(), equalTo("21"));
    }
}