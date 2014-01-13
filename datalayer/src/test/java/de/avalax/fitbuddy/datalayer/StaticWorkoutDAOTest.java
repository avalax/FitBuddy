package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StaticWorkoutDAOTest {
    @InjectMocks
    private StaticWorkoutDAO workoutDAO;

    @Test
    public void testSave_shouldDoNothing() throws Exception {
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);

        verifyNoMoreInteractions(workout);
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutDAO.load();

        assertThat(workout, instanceOf(Workout.class));
    }
}
