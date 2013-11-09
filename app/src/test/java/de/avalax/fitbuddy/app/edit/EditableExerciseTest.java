package de.avalax.fitbuddy.app.edit;

import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class EditableExerciseTest {
    @Test
    public void Exercise_ShouldBeSerializable() throws Exception {
        EditableExercise editableExercise = mock(EditableExercise.class);
        assertThat(editableExercise,instanceOf(Serializable.class));
    }
}
