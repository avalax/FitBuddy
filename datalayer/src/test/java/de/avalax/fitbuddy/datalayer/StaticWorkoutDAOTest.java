package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class StaticWorkoutDAOTest {

    @Test
    public void testSave_shouldDoNothing() throws Exception {
        WorkoutDAO workoutDAO = new StaticWorkoutDAO();
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);
        verifyNoMoreInteractions(workout);
    }

    @Test
    public void load_shouldReturnAWorkout() throws Exception {
        WorkoutDAO workoutDAO = new StaticWorkoutDAO();
        assertThat(workoutDAO.load(), instanceOf(Workout.class));
    }
}
