package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml", sdk=21)
public class SQLiteFinishedWorkoutRepositoryTest {
    private FinishedWorkoutRepository finishedWorkoutRepository;
    private Workout workout;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        FinishedExerciseRepository finishedExerciseRepository = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
        finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper, finishedExerciseRepository);
        workout = new BasicWorkout();
        workout.setWorkoutId(new WorkoutId("42"));
        workout.setName("basicWorkout");
    }

    @Test(expected = FinishedWorkoutException.class)
    public void loadWithNullInstance_shouldThrowWorkoutNotFoundException() throws Exception {
        finishedWorkoutRepository.load(null);
    }

    @Test(expected = FinishedWorkoutException.class)
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
        Exercise exercise = workout.getExercises().createExercise();
        exercise.setName("finished exercise");
        Set set = exercise.getSets().createSet();
        set.setWeight(42.21);
        set.setMaxReps(15);
        set.setReps(12);

        FinishedWorkoutId finishedWorkoutId = finishedWorkoutRepository.saveWorkout(workout);

        FinishedWorkout finishedWorkout = finishedWorkoutRepository.load(finishedWorkoutId);
        List<FinishedExercise> finishedExercises = finishedWorkout.getFinishedExercises();
        assertThat(finishedExercises, hasSize(1));
        assertThat(finishedExercises.get(0).getName(), equalTo(exercise.getName()));
        assertThat(finishedExercises.get(0).getWeight(), equalTo(exercise.getSets().get(0).getWeight()));
        assertThat(finishedExercises.get(0).getReps(), equalTo(exercise.getSets().get(0).getReps()));
        assertThat(finishedExercises.get(0).getMaxReps(), equalTo(exercise.getSets().get(0).getMaxReps()));
    }

    @Test
    public void noFinishedWorkouts_shouldReturnEmptyList() throws Exception {
        List<FinishedWorkout> finishedWorkouts = finishedWorkoutRepository.loadAll();

        assertThat(finishedWorkouts, emptyCollectionOf(FinishedWorkout.class));
    }

    @Test
    public void twoPersistedFinishedWorkouts_shouldReturnListWithThem() throws Exception {
        FinishedWorkoutId finishedWorkoutId1 = finishedWorkoutRepository.saveWorkout(new BasicWorkout());
        FinishedWorkoutId finishedWorkoutId2 = finishedWorkoutRepository.saveWorkout(new BasicWorkout());

        List<FinishedWorkout> finishedWorkouts = finishedWorkoutRepository.loadAll();

        assertThat(finishedWorkouts, hasSize(2));
        assertThat(finishedWorkouts.get(0).getFinishedWorkoutId(), Matchers.equalTo(finishedWorkoutId1));
        assertThat(finishedWorkouts.get(1).getFinishedWorkoutId(), Matchers.equalTo(finishedWorkoutId2));
    }
}