package de.avalax.fitbuddy.domain.model;

import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class SetTest {
    @Test
    public void Set_ShouldBeSerializable() throws Exception {
        Set set = mock(Set.class);
        assertThat(set,instanceOf(Serializable.class));
    }
}
