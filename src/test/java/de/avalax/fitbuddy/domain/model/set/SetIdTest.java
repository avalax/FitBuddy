package de.avalax.fitbuddy.domain.model.set;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SetIdTest {

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void testSameIdentity() throws Exception {
        assertThat(new SetId("42"), equalTo(new SetId("42")));
        assertThat(new SetId("42").hashCode(), equalTo(new SetId("42").hashCode()));
        assertThat(new SetId("42").equals("42"), is(false));
    }

    @Test
    public void testToString_shouldReturnId() throws Exception {
        assertThat((new SetId("42")).toString(), equalTo("SetId [id=42]"));
        assertThat(new SetId("21").toString(), equalTo("SetId [id=21]"));
    }

    @Test
    public void testId_shouldReturnId() throws Exception {
        assertThat((new SetId("42")).getId(), equalTo("42"));
        assertThat(new SetId("21").getId(), equalTo("21"));
    }
}