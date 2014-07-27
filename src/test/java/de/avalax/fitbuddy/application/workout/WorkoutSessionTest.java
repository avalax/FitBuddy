package de.avalax.fitbuddy.application.workout;

import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class WorkoutSessionTest {
    private WorkoutSession workoutSession;

    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        String lastWorkoutId = "21";
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit().putString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, lastWorkoutId).commit();
        workoutSession = new WorkoutSession(sharedPreferences);
    }

    @Test
    public void switchWorkout_shouldLoadAndSetWorkoutFromRepository() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");

        workoutSession.switchWorkoutById(workoutId);

        assertThat(sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "-1"), equalTo(workoutId.id()));
    }
}