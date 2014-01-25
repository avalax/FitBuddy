package de.avalax.fitbuddy.app;

import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class WorkoutSessionTest {
    WorkoutSession workoutSession;

    WorkoutDAO workoutDAO;

    private Workout workout;
    private String[] workouts;

    @Before
    public void setUp() throws Exception {
        workoutDAO = mock(WorkoutDAO.class);
        workout = mock(Workout.class);
        workouts =  new String[]{};
        int lastPosition = 21;
        when(workoutDAO.load(lastPosition)).thenReturn(workout);
        workoutSession = new WorkoutSession(workoutDAO, lastPosition);
    }

    @Test
    public void shouldInitialzeWithWorkoutFromWorkoutDAO() {
        assertThat(workoutSession.getWorkout(), equalTo(workout));
    }

    @Test
    public void testSaveWorkout_shouldCallWorkoutDAOSaveAndLoadNewWorkout() throws Exception {
        workoutSession.saveWorkout();

        verify(workoutDAO).save(workout);
    }

    @Test
    public void testGetWorkoutlist_shouldReturnWorkoutFromWorkoutDAO() throws Exception {
        when(workoutDAO.getWorkoutlist()).thenReturn(workouts);

        assertThat(workoutSession.getWorkoutlist(),is(workouts));
    }

    @Test
    public void testSwitchWorkout_shouldSwitchToWorkoutFromWorkoutDAO() throws Exception {
        int position = 42;
        Workout loadedWorkout = mock(Workout.class);
        when(workoutDAO.load(position)).thenReturn(loadedWorkout);

        workoutSession.switchWorkout(position);
        assertThat(workoutSession.getWorkout(), is(loadedWorkout));
    }
}
