package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;

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
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SQLiteExerciseRepositoryTest {

    private SQLiteWorkoutRepository workoutRepository;
    private ExerciseRepository exerciseRepository;
    private SetRepository setRepository;
    private Workout workout;

    private Exercise createExercise(String name) throws WorkoutException {
        Exercise exercise = workout.getExercises().createExercise();
        exercise.setName(name);
        workoutRepository.save(workout);
        return exercise;
    }

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteExerciseRepositoryTest", 1, context, R.raw.fitbuddy_db);
        setRepository = new SQLiteSetRepository(sqLiteOpenHelper);
        exerciseRepository = new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
        workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
        workout = new BasicWorkout();
    }

    @Test
    public void saveNewExercise_shouldAssignNewExerciseId() throws Exception {
        Exercise exercise = workout.getExercises().createExercise();

        assertThat(exercise.getExerciseId(), nullValue());
        workoutRepository.save(workout);

        assertThat(exercise.getExerciseId(), any(ExerciseId.class));
    }

    @Test
    public void savePersistedExercise_shouldKeepExerciseId() throws Exception {
        Exercise exercise = workout.getExercises().createExercise();
        workoutRepository.save(workout);
        ExerciseId exerciseId = exercise.getExerciseId();

        workoutRepository.save(workout);
        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void updateExercises_shouldSaveTheCorrectEntity() throws Exception {
        Exercise exercise1 = createExercise("name1");
        Exercise exercise2 = createExercise("name2");
        WorkoutId workoutId = workout.getWorkoutId();

        List<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        exercises.get(1).setName("newname2");
        exerciseRepository.save(workout.getWorkoutId(), exercises.get(1));

        List<Exercise> reloadedExercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        assertThat(reloadedExercises.get(0), equalTo(exercise1));
        assertThat(reloadedExercises.get(0).getName(), equalTo("name1"));
        assertThat(reloadedExercises.get(1), equalTo(exercise2));
        assertThat(reloadedExercises.get(1).getName(), equalTo("newname2"));
    }

    @Test
    public void saveExercise_shouldAlsoSaveSets() throws Exception {
        Exercise exercise = createExercise("exercise with a set");
        Set set = exercise.getSets().createSet();
        WorkoutId workoutId = workout.getWorkoutId();

        exerciseRepository.save(workoutId, exercise);

        List<Set> loadedSets = setRepository.allSetsBelongsTo(exercise.getExerciseId());
        assertThat(loadedSets.size(), equalTo(1));
        assertThat(loadedSets.get(0), equalTo(set));
    }

    @Test
    public void loadAllExercisesBelongsTo_shouldAddSetsToExercise() throws Exception {
        Exercise exercise = createExercise("an exercise with two sets");
        Set set1 = exercise.getSets().createSet();
        Set set2 = exercise.getSets().createSet();
        WorkoutId workoutId = workout.getWorkoutId();

        exerciseRepository.save(workoutId, exercise);

        List<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        assertThat(exercises.get(0).getSets().size(), equalTo(2));
        assertThat(exercises.get(0).getSets().get(0), equalTo(set1));
        assertThat(exercises.get(0).getSets().get(1), equalTo(set2));
    }

    @Test
    public void deleteExerciseWithNull_shouldDoNothing() throws Exception {
        exerciseRepository.delete(null);
    }

    @Test
    public void deleteExerciseByExerciseId_shouldRemoveItFromPersistence() throws Exception {
        Exercise exercise = createExercise("name");
        WorkoutId workoutId = workout.getWorkoutId();

        exerciseRepository.delete(exercise.getExerciseId());

        assertThat(exerciseRepository.allExercisesBelongsTo(workoutId).size(), equalTo(0));
    }
}