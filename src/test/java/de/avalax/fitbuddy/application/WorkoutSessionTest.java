package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class WorkoutSessionTest {
    WorkoutSession workoutSession;

    WorkoutRepository workoutRepository;

    private Workout workout;
    private SharedPreferences.Editor editor;

    @Before
    public void setUp() throws Exception {
        workoutRepository = mock(WorkoutRepository.class);
        workout = mock(Workout.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        String id = "21";
        when(sharedPreferences.getString(WorkoutSession.LAST_WORKOUT_POSITION, "1")).thenReturn(id);
        when(workoutRepository.load(new WorkoutId(id))).thenReturn(workout);
        when(sharedPreferences.edit()).thenReturn(editor);
        WorkoutFactory workoutFactory = new WorkoutFactory();
        workoutSession = new WorkoutSession(sharedPreferences, workoutRepository, workoutFactory);
    }

    @Test
    public void shouldInitialzeWithWorkoutFromWorkoutDAO() {
        assertThat(workoutSession.getWorkout(), equalTo(workout));
    }

    @Test
    @Ignore("Add a workoutSessionDAO")
    public void testSaveWorkout_shouldCallWorkoutDAOSaveAndLoadNewWorkout() throws Exception {
        workoutSession.saveWorkoutSession();

        verify(workoutRepository).save(workout);
    }

    @Test
    public void testSwitchWorkout_shouldSwitchToWorkoutFromWorkoutDAO() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout loadedWorkout = mock(Workout.class);
        when(workoutRepository.load(workoutId)).thenReturn(loadedWorkout);

        workoutSession.switchWorkout(workoutId);
        verify(editor).putString(WorkoutSession.LAST_WORKOUT_POSITION, workoutId.id());
        verify(editor).commit();
        assertThat(workoutSession.getWorkout(), is(loadedWorkout));
    }
}