package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.WorkoutSet;
import de.avalax.fitbuddy.workout.exceptions.SetNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasicWorkoutSetTest {

    WorkoutSet workoutSet;
    List<Set> sets;

    @Before
    public void setUp() throws Exception{
        sets = new ArrayList<Set>();
        workoutSet = new BasicWorkoutSet("Bankdrücken",sets);
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNameBankdrücken() throws Exception {
        assertThat(workoutSet.getName(), equalTo("Bankdrücken"));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNumberOfSets() throws Exception {
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));
        sets.add(mock(Set.class));

        assertThat(workoutSet.getSetSize(), equalTo(4));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnTendencyPlus() throws Exception {
        workoutSet.setTendency(Tendency.PLUS);

        assertThat(workoutSet.getTendency(), equalTo(Tendency.PLUS));
    }

    @Test
    public void BasicWorkout_ShouldReturnRepetitionsFromWorkoutSet() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);
        when(set.getRepetitions()).thenReturn(12);

        assertThat(workoutSet.getRepetitions(),equalTo(12));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnCurrentSetOnStartup() throws Exception {
        Set set = mock(Set.class);
        sets.add(set);

        assertThat(workoutSet.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnSecondSetAsCurrentSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        workoutSet.setSetNumber(2);

        assertThat(workoutSet.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnPreviousSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(set);
        sets.add(mock(Set.class));

        workoutSet.setSetNumber(2);

        assertThat(workoutSet.getPreviousSet(), equalTo(set));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNextSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        workoutSet.setSetNumber(1);

        assertThat(workoutSet.getNextSet(), equalTo(set));
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicWorkoutSet_ShouldThrowExceptionWithNoPreviousSetAvailable() throws Exception {
        sets.add(mock(Set.class));

        workoutSet.setSetNumber(1);

        workoutSet.getPreviousSet();
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicWorkoutSet_ShouldThrowExceptionWithNoNextSetAvailable() throws Exception {
        sets.add(mock(Set.class));

        workoutSet.setSetNumber(1);

        workoutSet.getNextSet();
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNextSetOnIncrementSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        workoutSet.incrementSetNumber();

        assertThat(workoutSet.getCurrentSet(), equalTo(set));
        assertThat(workoutSet.getSetNumber(), equalTo(2));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNextSetOnSettingRepetitionsForCurrentSet() throws Exception {
        Set set = mock(Set.class);

        sets.add(mock(Set.class));
        sets.add(set);

        workoutSet.setRepetitions(12);

        assertThat(workoutSet.getCurrentSet(), equalTo(set));
        assertThat(workoutSet.getSetNumber(), equalTo(2));
    }

    @Test(expected = SetNotAvailableException.class)
    public void BasicWorkoutSet_ShouldThrowExceptionWhenSettingRepetitionsOnLastSet() throws Exception {
        sets.add(mock(Set.class));

        workoutSet.setRepetitions(12);
    }
}
