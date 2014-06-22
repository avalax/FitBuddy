package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.application.R;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteExerciseRepositoryTest {
    private ExerciseRepository exerciseRepository;
    private SetRepository setRepository;
    private WorkoutId workoutId;

    private void createWorkout(FitbuddySQLiteOpenHelper sqLiteOpenHelper) {
        WorkoutRepository workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());
        workoutRepository.save(workout);
        workoutId = workout.getWorkoutId();
    }

    @Before
    public void setUp() throws Exception {
        ManageWorkoutActivity manageWorkoutActivity = Robolectric.buildActivity(ManageWorkoutActivity.class).create().get();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper(1, manageWorkoutActivity, R.raw.fitbuddy_db);
        setRepository = mock(SetRepository.class);
        exerciseRepository = new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);

        createWorkout(sqLiteOpenHelper);
    }

    @Test
    public void saveUnpersistedExercise_shouldAssignNewExerciseId() throws Exception {
        Exercise exercise = new BasicExercise("nam", new ArrayList<Set>());

        assertThat(exercise.getExerciseId(), nullValue());
        exerciseRepository.save(workoutId, exercise);
        assertThat(exercise.getExerciseId(), any(ExerciseId.class));
    }

    @Test
    public void savePersistedExercise_shouldKeepExerciseId() {
        Exercise exercise = new BasicExercise("nam", new ArrayList<Set>());
        exerciseRepository.save(workoutId, exercise);
        ExerciseId exerciseId = exercise.getExerciseId();

        exerciseRepository.save(workoutId, exercise);
        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void saveExercise_shouldAlsoSaveSets() {
        List<Set> sets = new ArrayList<>();
        Set set = new BasicSet(42, 12);
        sets.add(set);
        Exercise exercise = new BasicExercise("nam", sets);

        exerciseRepository.save(workoutId, exercise);

        verify(setRepository).save(exercise.getExerciseId(), set);
    }

    @Test
    public void loadByUnknownExerciseId_shouldReturnNullValue() {
        Exercise exercise = exerciseRepository.load(new ExerciseId("21"));
        assertThat(exercise, nullValue());
    }

    @Test
    public void loadByExerciseId_shouldReturnExerciseWithExerciseId() {
        Exercise exercise = new BasicExercise("name", new ArrayList<Set>());
        exerciseRepository.save(workoutId, exercise);
        ExerciseId exerciseId = exercise.getExerciseId();

        Exercise loadExercise = exerciseRepository.load(exerciseId);
        assertThat(loadExercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    @Ignore("assertSets")
    public void loadByExerciseId_shouldReturnExerciseWithSets() {
        ArrayList<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(42, 12));
        sets.add(new BasicSet(40, 10));
        Exercise exercise = new BasicExercise("name", sets);

        exerciseRepository.save(workoutId, exercise);
        ExerciseId exerciseId = exercise.getExerciseId();

        Exercise loadExercise = exerciseRepository.load(exerciseId);
        assertThat(loadExercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    @Ignore("imlement loadAllExercisesBelongsTo_shouldReturnExercisesOfWorkout")
    public void loadAllExercisesBelongsTo_shouldReturnExercisesOfWorkout() throws Exception {
        fail();
    }

    @Test
    public void deleteExerciseByExerciseId_shouldRemoveItFromPersistence() throws Exception {
        Exercise exercise = new BasicExercise("name", new ArrayList<Set>());
        exerciseRepository.save(workoutId, exercise);
        ExerciseId exerciseId = exercise.getExerciseId();

        exerciseRepository.delete(exerciseId);

        assertThat(exerciseRepository.load(exerciseId), nullValue());
    }
}