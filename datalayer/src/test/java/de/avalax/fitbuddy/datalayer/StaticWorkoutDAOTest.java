package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StaticWorkoutDAOTest {
    @InjectMocks
    private StaticWorkoutDAO workoutDAO;
    private int anyPosition = 42;

    @Test
    public void testSave_shouldDoNothing() throws Exception {
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);

        verifyNoMoreInteractions(workout);
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutDAO.load(anyPosition);

        assertThat(workout, instanceOf(Workout.class));
    }

    @Test
    public void testGetWorkouts_shouldReturnAListOfWorkouts() throws Exception {
        String[] workouts = workoutDAO.getWorkoutlist();

        assertThat(workouts.length, is(not(0)));
    }
}
