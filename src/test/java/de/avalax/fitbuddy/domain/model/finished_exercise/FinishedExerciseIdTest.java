package de.avalax.fitbuddy.domain.model.finished_exercise;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FinishedExerciseIdTest {
    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testSameIdentity() throws Exception {
        assertThat(new FinishedExerciseId("42"), equalTo(new FinishedExerciseId("42")));
        assertThat(new FinishedExerciseId("42").hashCode(), equalTo(new FinishedExerciseId("42").hashCode()));
        assertThat(new FinishedExerciseId("42").equals("42"), is(false));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new FinishedExerciseId("42")).toString(), equalTo("FinishedExerciseId [id=42]"));
        assertThat(new FinishedExerciseId("21").toString(), equalTo("FinishedExerciseId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new FinishedExerciseId("42")).getId(), equalTo("42"));
        assertThat(new FinishedExerciseId("21").getId(), equalTo("21"));
    }
}