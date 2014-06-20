package de.avalax.fitbuddy.domain.model.exercise;


import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class ExerciseTest {

    @Test
    public void Exercise_ShouldBeSerializable() throws Exception {
        Exercise exercise = mock(Exercise.class);
        assertThat(exercise, instanceOf(Serializable.class));
    }
}
