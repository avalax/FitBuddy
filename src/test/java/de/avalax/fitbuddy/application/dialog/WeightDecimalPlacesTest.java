package de.avalax.fitbuddy.application.dialog;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class WeightDecimalPlacesTest {

    private WeightDecimalPlaces weightDecimalPlaces;

    private void assertLabel(int position, String label) {
        String[] labels = weightDecimalPlaces.getLabels();
        assertThat(labels[position], equalTo(label));
    }

    private void assertWeight(int position, double weight) {
        assertThat(weightDecimalPlaces.getWeight(position), equalTo(weight));
    }

    private void assertPosition(double weight, int position) {
        assertThat(weightDecimalPlaces.getPosition(weight), equalTo(position));
    }

    @Before
    public void setUp() throws Exception {
        weightDecimalPlaces = new WeightDecimalPlaces();
    }

    @Test
    public void testGetLabels() throws Exception {
        assertThat(weightDecimalPlaces.getLabels().length, is(8));
        assertLabel(0, "0");
        assertLabel(1, "125");
        assertLabel(2, "250");
        assertLabel(3, "375");
        assertLabel(4, "500");
        assertLabel(5, "625");
        assertLabel(6, "750");
        assertLabel(7, "875");
    }

    @Test
    public void testGetWeight() throws Exception {
        assertWeight(0, 0.0);
        assertWeight(1, 0.125);
        assertWeight(2, 0.25);
        assertWeight(3, 0.375);
        assertWeight(4, 0.5);
        assertWeight(5, 0.625);
        assertWeight(6, 0.75);
        assertWeight(7, 0.875);
    }

    @Test
    public void testGetPosition() throws Exception {
        assertPosition(-1, 0);
        assertPosition(0.0, 0);
        assertPosition(0.125, 1);
        assertPosition(0.25, 2);
        assertPosition(0.375, 3);
        assertPosition(0.5, 4);
        assertPosition(0.625, 5);
        assertPosition(0.75, 6);
        assertPosition(0.875, 7);
    }
}