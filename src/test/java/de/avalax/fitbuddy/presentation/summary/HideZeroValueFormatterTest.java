package de.avalax.fitbuddy.presentation.summary;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HideZeroValueFormatterTest {
    @Test
    public void testGetFormattedValue() throws Exception {
        HideZeroValueFormatter formatter = new HideZeroValueFormatter();

        assertThat(formatter.getFormattedValue(0f,null,0,null)).isEqualTo("");
        assertThat(formatter.getFormattedValue(1f,null,0,null)).isEqualTo("1");
        assertThat(formatter.getFormattedValue(10.0f,null,0,null)).isEqualTo("10");
    }
}