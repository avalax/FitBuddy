package de.avalax.fitbuddy.application.swipeBar;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class WeightRaiseCalculatorTest {

    private WeightRaiseCalculator weightRaiseCalculator;

    @Before
    public void setUp() throws Exception {
        weightRaiseCalculator = new WeightRaiseCalculator();
    }

    @Test
    public void testCalculate_shouldReturn0() throws Exception {
        double currentWeightRaise = 0;
        int modification = 0;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.0));
    }

    @Test
    public void testCalculate_shouldReturn0WhenGoesNegative() throws Exception {
        double currentWeightRaise = 0.0;
        int modification = -1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.0));
    }

    @Test
    public void testCalculate_shouldReturn025WhenModificationFrom0Is1() throws Exception {
        double currentWeightRaise = 0.0;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.25));
    }

    @Test
    public void testCalculate_shouldReturn05WhenModificationFrom0Is2() throws Exception {
        double currentWeightRaise = 0.0;
        int modification = 2;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.5));
    }

    @Test
    public void testCalculate_shouldReturn075WhenModificationFrom05Is1() throws Exception {
        double currentWeightRaise = 0.5;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.75));
    }

    @Test
    public void testCalculate_shouldReturn125WhenModificationFrom075Is1() throws Exception {
        double currentWeightRaise = 0.75;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(1.25));
    }

    @Test
    public void testCalculate_shouldReturn25WhenModificationFrom125Is1() throws Exception {
        double currentWeightRaise = 1.25;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(2.5));
    }

    @Test
    public void testCalculate_shouldReturn5WhenModificationFrom25Is1() throws Exception {
        double currentWeightRaise = 2.5;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(5.0));
    }

    @Test
    public void testCalculate_shouldReturn75WhenModificationFrom5Is1() throws Exception {
        double currentWeightRaise = 5.0;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(7.5));
    }

    @Test
    public void testCalculate_shouldReturn10WhenModificationFrom75Is1() throws Exception {
        double currentWeightRaise = 7.5;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(10.0));
    }

    @Test
    public void testCalculate_shouldReturn125WhenModificationFrom10Is1() throws Exception {
        double currentWeightRaise = 10.0;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(12.5));
    }

    @Test
    public void testCalculate_shouldReturn15WhenModificationFrom125Is1() throws Exception {
        double currentWeightRaise = 12.5;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(15.0));
    }

    @Test
    public void testCalculate_shouldReturn20WhenModificationFrom15Is1() throws Exception {
        double currentWeightRaise = 15.0;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(20.0));
    }

    @Test
    public void testCalculate_shouldReturn20WhenModificationFrom20Is1() throws Exception {
        double currentWeightRaise = 20.0;
        int modification = 1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(20.0));
    }

    @Test
    public void testCalculate_shouldReturn125WhenModificationFrom25IsMinus1() throws Exception {
        double currentWeightRaise = 2.5;
        int modification = -1;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(1.25));
    }

    @Test
    public void testCalculate_shouldReturn075WhenModificationFrom25IsMinus2() throws Exception {
        double currentWeightRaise = 2.5;
        int modification = -2;

        double newWeightRaise = weightRaiseCalculator.calculate(currentWeightRaise, modification);

        assertThat(newWeightRaise, equalTo(0.75));
    }
}
