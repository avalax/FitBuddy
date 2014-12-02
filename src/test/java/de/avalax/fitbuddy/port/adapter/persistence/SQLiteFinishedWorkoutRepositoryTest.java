package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutNotFoundException;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(RobolectricTestRunner.class)
public class SQLiteFinishedWorkoutRepositoryTest {
    private FinishedWorkoutRepository finishedWorkoutRepository;
    private Workout workout;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        FinishedExerciseRepository finishedExerciseRepository = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
        finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper, finishedExerciseRepository);
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
        Exercise exercise = workout.createExercise();
        exercise.setName("finished exercise");
        Set set = exercise.createSet();
        set.setWeight(42.21);
        set.setMaxReps(15);
        set.setReps(12);

        FinishedWorkoutId finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workout);

        FinishedWorkout finishedWorkout = finishedWorkoutRepository.load(finishedWorkoutId);
        List<FinishedExercise> finishedExercises = finishedWorkout.getFinishedExercises();
        assertThat(finishedExercises, hasSize(1));
        assertThat(finishedExercises.get(0).getName(), equalTo(exercise.getName()));
        assertThat(finishedExercises.get(0).getWeight(), equalTo(exercise.setAtPosition(0).getWeight()));
        assertThat(finishedExercises.get(0).getReps(), equalTo(exercise.setAtPosition(0).getReps()));
        assertThat(finishedExercises.get(0).getMaxReps(), equalTo(exercise.setAtPosition(0).getMaxReps()));
    }
}