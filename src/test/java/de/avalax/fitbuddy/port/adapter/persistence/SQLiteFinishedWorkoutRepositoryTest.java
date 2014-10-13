package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.presentation.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteFinishedWorkoutRepositoryTest {
    private SQLiteFinishedWorkoutRepository finishedWorkoutRepository;
    private Workout workout;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper);
        workout = new BasicWorkout();
        workout.setWorkoutId(new WorkoutId("42"));
        workout.setName("basicWorkout");
    }

    @Test(expected = FinishedWorkoutNotFoundException.class)
    public void loadWithNullInstance_shouldThrowWorkoutNotFoundException() throws Exception {
        finishedWorkoutRepository.load(null);
    }

    @Test(expected = FinishedWorkoutNotFoundException.class)
    public void loadByUnknownWorkoutId_shouldThrowWorkoutNotFoundException() throws Exception {
        finishedWorkoutRepository.load(new FinishedWorkoutId("21"));
    }

    @Test
    public void saveWorkout_shouldInsertWorkoutWithNewFinishedWorkoutId() throws Exception {
        FinishedWorkoutId finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workout);

        assertThat(finishedWorkoutId, any(FinishedWorkoutId.class));
    }

    @Test
    public void saveWorkout_shouldInsertWorkoutInformationsIntoDatabase() throws Exception {
        FinishedWorkoutId finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workout);

        FinishedWorkout finishedWorkout = finishedWorkoutRepository.load(finishedWorkoutId);

        assertThat(finishedWorkout.getFinishedWorkoutId(), equalTo(finishedWorkoutId));
        assertThat(finishedWorkout.getWorkoutId(), equalTo(workout.getWorkoutId()));
        assertThat(finishedWorkout.getName(), equalTo(workout.getName()));
        assertThat(finishedWorkout.getCreated(), any(String.class));
    }

    @Test
    public void saveWorkout_shouldAlsoInsertExerciseInformationsIntoDatabase() throws Exception {
        //TODO: save exercises with set details
    }
}