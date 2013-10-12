package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasicExerciseTest {

    Exercise exercise;
    List<Set> sets;

    @Before
    public void setUp() throws Exception{
        sets = new ArrayList<>();
        exercise = new BasicExercise("Bankdrücken",sets, 2.5);
    }

    @Test
    public void BasicExercise_ShouldReturnNameBankdrücken() throws Exception {
        assertThat(exercise.getName(), equalTo("Bankdrücken"));
    }

    @Test
    public void BasicExercise_ShouldReturnNumberOfSets() throws Exception {
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));

        assertThat(exercise.getSetSize(), equalTo(4));
    }

    @Test
    public void BasicExercise_ShouldReturnTendencyPlus() throws Exception {
        exercise.setTendency(Tendency.PLUS);

        assertThat(exercise.getTendency(), equalTo(Tendency.PLUS));
    }

    @Test
    public void BasicExercise_ShouldReturnRepsFromSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);
        when(set.getReps()).thenReturn(12);

        assertThat(exercise.getReps(),equalTo(12));
    }

    @Test
    public void BasicExercise_ShouldReturnCurrentSetOnStartup() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        assertThat(exercise.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicExercise_ShouldReturnSecondSetAsCurrentSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.setSetNumber(2);

        assertThat(exercise.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicExercise_ShouldReturnPreviousSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(set);
        sets.add(mock(Set.class));

        exercise.setSetNumber(2);

        assertThat(exercise.getPreviousSet(), equalTo(set));
    }

    @Test
    public void BasicExercise_ShouldReturnNextSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.setSetNumber(1);

        assertThat(exercise.getNextSet(), equalTo(set));
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicExercise_ShouldThrowExceptionWithNoPreviousSetAvailable() throws Exception {
        sets.add(mock(Set.class));

        exercise.setSetNumber(1);

        exercise.getPreviousSet();
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicExercise_ShouldThrowExceptionWithNoNextSetAvailable() throws Exception {
        sets.add(mock(Set.class));

        exercise.setSetNumber(1);

        exercise.getNextSet();
    }

    @Test
    public void BasicExercise_ShouldReturnNextSetOnIncrementSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.incrementSet();

        assertThat(exercise.getCurrentSet(), equalTo(set));
        assertThat(exercise.getSetNumber(), equalTo(2));
    }

    @Test
    public void BasicExercise_ShouldReturnNextSetOnSettingRepsForCurrentSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        exercise.setReps(12);

        assertThat(exercise.getCurrentSet(), equalTo(set));
        assertThat(exercise.getSetNumber(), equalTo(2));
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicExercise_ShouldThrowExceptionWhenSettingRepsOnLastSet() throws Exception {
        sets.add(mock(Set.class));

        exercise.setReps(12);
    }

    @Test
    public void BasicExercise_ShouldReturnWeightOfCurrentSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        when(set.getWeight()).thenReturn(50.0);

        assertThat(exercise.getWeight(), equalTo(50.0));
    }

    @Test
    public void BasicExercise_ShouldGetWeightRaiseForNeutralTendency() throws Exception {
        exercise = new BasicExercise("NeutralTendency",sets,5.0);
        Set set = mock(Set.class);
        sets.add(set);

        when(set.getWeight()).thenReturn(2.5);

        assertThat(exercise.getWeightRaise(Tendency.NEUTRAL),equalTo(2.5));
    }

    @Test
    public void BasicExercise_ShouldGetWeightRaiseForPositiveTendency() {
        exercise = createExercise(2.5, 5.0);

        assertThat(exercise.getWeightRaise(Tendency.PLUS),equalTo(7.5));
    }

    @Test
    public void BasicExercise_ShouldGetWeightRaiseForMinusTendency() {
        exercise = createExercise(15.0, 5.0);

        assertThat(exercise.getWeightRaise(Tendency.MINUS),equalTo(10.0));
    }

    @Test
    public void BasicExercise_ShouldGetWeightRaiseForMinusTendencyWhenRaiseWhouldBeNegative() {
        exercise = createExercise(2.5, 5.0);

        assertThat(exercise.getWeightRaise(Tendency.MINUS),equalTo(0.0));
    }

    private Exercise createExercise(double weight, double weightRaise) {
        Exercise exercise = new BasicExercise("MinusTendency",sets,weightRaise);
        Set set = mock(Set.class);
        sets.add(set);

        when(set.getWeight()).thenReturn(weight);

        return exercise;
    }
}
