package de.avalax.fitbuddy.domain.model.exercise;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExerciseIdTest {

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testSameIdentity() throws Exception {
        assertThat(new ExerciseId("42"), equalTo(new ExerciseId("42")));
        assertThat(new ExerciseId(new ExerciseId("42")), equalTo(new ExerciseId("42")));
        assertThat(new ExerciseId("42").hashCode(), equalTo(new ExerciseId("42").hashCode()));
        assertThat(new ExerciseId("42").equals("42"), is(false));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new ExerciseId("42")).toString(), equalTo("ExerciseId [id=42]"));
        assertThat(new ExerciseId("21").toString(), equalTo("ExerciseId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new ExerciseId("42")).id(), equalTo("42"));
        assertThat(new ExerciseId("21").id(), equalTo("21"));
    }
}