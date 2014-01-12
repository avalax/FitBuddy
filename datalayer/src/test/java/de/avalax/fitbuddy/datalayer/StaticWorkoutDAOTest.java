package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Test;

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
}
