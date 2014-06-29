package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.workout.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class WorkoutSessionTest {
    private WorkoutSession workoutSession;

    private WorkoutRepository workoutRepository;

    private SharedPreferences sharedPreferences;

    private String lastWorkoutId;

    @Before
    public void setUp() throws Exception {
        lastWorkoutId = "21";
        workoutRepository = mock(WorkoutRepository.class);
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit().putString(WorkoutSession.LAST_WORKOUT_ID, lastWorkoutId).commit();
        WorkoutId workoutId = new WorkoutId(lastWorkoutId);
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);
        when(workoutRepository.load(workoutId)).thenReturn(workout);
        workoutSession = new WorkoutSession(sharedPreferences, workoutRepository);
    }

    @Test
    public void switchToLastWorkout_shouldReturnWorkoutWithLastWorkoutId() {
        workoutSession.switchToLastLoadedWorkout();
        assertThat(workoutSession.getWorkout().getWorkoutId().id(), equalTo(lastWorkoutId));
    }

    @Test
    @Ignore("Add a workoutSessionRepository")
    public void testSaveWorkout_shouldSaveWorkoutToRepository() throws Exception {
        workoutSession.saveWorkoutSession();
    }

    @Test
    public void switchWorkout_shouldLoadAndSetWorkoutFromRepository() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout loadedWorkout = mock(Workout.class);
        when(workoutRepository.load(workoutId)).thenReturn(loadedWorkout);

        workoutSession.switchWorkoutById(workoutId);

        assertThat(sharedPreferences.getString(WorkoutSession.LAST_WORKOUT_ID, "-1"), equalTo(workoutId.id()));
        assertThat(workoutSession.getWorkout(), is(loadedWorkout));
    }
}