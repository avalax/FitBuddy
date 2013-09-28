package de.avalax.fitbuddy.workout;


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
    public void BasicWorkout_SetRepetitionsTo0() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        Set set = mock(Set.class);

        when(workoutSet.getCurrentSet()).thenReturn(set);

        workout.setRepetitions(0);
        verify(set).setRepetitions(0);
        assertThat(workout.getRepetitions(), equalTo(0));
    }

    @Test
    public void BasicWorkout_SetRepetitionsTo12() throws Exception {
        WorkoutSet workoutSet = mock(WorkoutSet.class);
        workoutSets.add(workoutSet);
        Set set = mock(Set.class);

        when(workoutSet.getCurrentSet()).thenReturn(set);
        when(set.getRepetitions()).thenReturn(12);

        workout.setRepetitions(12);
        verify(set).setRepetitions(12);
        assertThat(workout.getRepetitions(), equalTo(12));
    }
}