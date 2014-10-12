package de.avalax.fitbuddy.domain.model.finishedWorkout;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class FinishedWorkoutIdTest {
    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testSameIdentity() throws Exception {
        assertThat(new FinishedWorkoutId("42"), equalTo(new FinishedWorkoutId("42")));
        assertThat(new FinishedWorkoutId(new FinishedWorkoutId("42")), equalTo(new FinishedWorkoutId("42")));
        assertThat(new FinishedWorkoutId("42").hashCode(), equalTo(new FinishedWorkoutId("42").hashCode()));
        assertThat(new FinishedWorkoutId("42").equals("42"), is(false));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new FinishedWorkoutId("42")).toString(), equalTo("FinishedWorkoutId [id=42]"));
        assertThat(new FinishedWorkoutId("21").toString(), equalTo("FinishedWorkoutId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new FinishedWorkoutId("42")).id(), equalTo("42"));
        assertThat(new FinishedWorkoutId("21").id(), equalTo("21"));
    }
}