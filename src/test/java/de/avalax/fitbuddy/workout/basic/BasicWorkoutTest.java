package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.WorkoutSet;
import de.avalax.fitbuddy.workout.exceptions.WorkoutSetNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class BasicWorkoutTest {

    private Workout workout;
    private List<WorkoutSet> workoutSets;

    @Before
    public void setUp() throws Exception {
        workoutSets = new ArrayList<WorkoutSet>();
        workout = new BasicWorkout(workoutSets);
    }

    @Test
    public void BasicWorkout_ShouldReturnCurrentWorkoutSetOnStartup() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);

        assertThat(workout.getCurrentWorkoutSet(), equalTo(workoutSet));
    }

    @Test
    public void BasicWorkout_ShouldReturnSecondWorkoutSetAsCurrentWorkoutSet() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);

        workoutSets.add(mock(WorkoutSet.class));
        workoutSets.add(workoutSet);

        workout.setWorkoutSetNumber(2);

        assertThat(workout.getCurrentWorkoutSet(), equalTo(workoutSet));
    }

    @Test
    public void BasicWorkout_ShouldReturnPreviousWorkoutSet() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);

        workoutSets.add(workoutSet);
        workoutSets.add(mock(WorkoutSet.class));

        workout.setWorkoutSetNumber(2);

        assertThat(workout.getPreviousWorkoutSet(), equalTo(workoutSet));
    }

    @Test
    public void BasicWorkout_ShouldReturnNextWorkoutSet() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(mock(WorkoutSet.class));

        workoutSets.add(workoutSet);

        workout.setWorkoutSetNumber(1);

        assertThat(workout.getNextWorkoutSet(), equalTo(workoutSet));
    }

    @Test(expected = WorkoutSetNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWithNoPreviousWorkoutSetAvailable() throws Exception {
        workoutSets.add(mock(WorkoutSet.class));

        workout.setWorkoutSetNumber(1);

        workout.getPreviousWorkoutSet();
    }

    @Test(expected = WorkoutSetNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWithNoNextWorkoutSetAvailable() throws Exception {
        workoutSets.add(mock(WorkoutSet.class));

        workout.setWorkoutSetNumber(1);

        workout.getNextWorkoutSet();
    }

    @Test
    public void BasicWorkout_ShouldReturnCurrentSetOnStartup() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        Set set = mock(Set.class);

        when(workoutSet.getCurrentSet()).thenReturn(set);

        assertThat(workout.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicWorkout_ShouldSetRepetitionsto12() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);

        workout.setRepetitions(12);

        verify(workoutSet).setRepetitions(12);
    }

    @Test
    public void BasicWorkout_ShouldReturnRepetitionsFromWorkoutSet() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        when(workoutSet.getRepetitions()).thenReturn(12);

        assertThat(workout.getRepetitions(),equalTo(12));
    }

    @Test
    public void BasicWorkout_ShouldSetTendencyInWorkoutSet() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        workoutSets.add(mock(WorkoutSet.class));

        workout.setTendency(Tendency.NEUTRAL);

        verify(workoutSet).setTendency(Tendency.NEUTRAL);
    }

    @Test
    public void BasicWorkout_ShouldSwitchToTheNextWorkout() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(mock(WorkoutSet.class));
        workoutSets.add(workoutSet);

        workout.incrementWorkoutSetNumber();

        assertThat(workout.getCurrentWorkoutSet(),equalTo(workoutSet));
    }

    @Test(expected = WorkoutSetNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenSwitchingToTheNextWorkoutWhenLastWorkoutSet() throws Exception {
        workoutSets.add(mock(WorkoutSet.class));

        workout.incrementWorkoutSetNumber();
    }

    @Test
    public void BasicWorkout_ShouldSwitchToThePreviousWorkout() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        workoutSets.add(mock(WorkoutSet.class));

        workout.setWorkoutSetNumber(2);

        workout.decrementWorkoutSetNumber();

        assertThat(workout.getCurrentWorkoutSet(),equalTo(workoutSet));
    }

    @Test(expected = WorkoutSetNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenSwitchingToThePreviousWorkoutWhenFirstWorkoutSet() throws Exception {
        workoutSets.add(mock(WorkoutSet.class));

        workout.decrementWorkoutSetNumber();
    }

    @Test
    public void BasicWorkout_ShouldSwitchToTheNextWorkoutSetWhenSettingTendency() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(mock(WorkoutSet.class));
        workoutSets.add(workoutSet);

        workout.setTendency(Tendency.MINUS);

        assertThat(workout.getCurrentWorkoutSet(),equalTo(workoutSet));
    }
}
