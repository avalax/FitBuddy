package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class WorkoutSessionTest {
    WorkoutSession workoutSession;

    WorkoutDAO workoutDAO;

    private Workout workout;
    private SharedPreferences.Editor editor;

    @Before
    public void setUp() throws Exception {
        workoutDAO = mock(WorkoutDAO.class);
        workout = mock(Workout.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        long lastPosition = 21L;
        when(sharedPreferences.getLong(WorkoutSession.LAST_WORKOUT_POSITION, 1L)).thenReturn(lastPosition);
        when(workoutDAO.load(lastPosition)).thenReturn(workout);
        when(sharedPreferences.edit()).thenReturn(editor);
        workoutSession = new WorkoutSession(sharedPreferences, workoutDAO);
    }

    @Test
    public void shouldInitialzeWithWorkoutFromWorkoutDAO() {
        assertThat(workoutSession.getWorkout(), equalTo(workout));
    }

    @Test
    @Ignore("Add a workoutSessionDAO")
    public void testSaveWorkout_shouldCallWorkoutDAOSaveAndLoadNewWorkout() throws Exception {
        workoutSession.saveWorkoutSession();

        verify(workoutDAO).save(workout);
    }

    @Test
    public void testSwitchWorkout_shouldSwitchToWorkoutFromWorkoutDAO() throws Exception {
        long position = 42;
        Workout loadedWorkout = mock(Workout.class);
        when(workoutDAO.load(position)).thenReturn(loadedWorkout);

        workoutSession.switchWorkout(position);
        verify(editor).putLong(WorkoutSession.LAST_WORKOUT_POSITION, position);
        verify(editor).commit();
        assertThat(workoutSession.getWorkout(), is(loadedWorkout));
    }
}