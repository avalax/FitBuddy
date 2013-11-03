package de.avalax.fitbuddy.edit;

import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class EditExerciseTest {
    @Test
    public void Exercise_ShouldBeSerializable() throws Exception {
        EditExercise editExercise = mock(EditExercise.class);
        assertThat(editExercise,instanceOf(Serializable.class));
    }
}
