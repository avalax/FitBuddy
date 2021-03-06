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
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SQLiteWorkoutRepositoryTest {

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private WorkoutId createWorkout(String name) throws ResourceException {
        Workout workout = new BasicWorkout();
        workout.setName(name);
        Exercise exercise = workout.getExercises().createExercise();
        exercise.getSets().createSet();
        workoutRepository.save(workout);
        return workout.getWorkoutId();
    }

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteWorkoutRepositoryTest", 1, context, R.raw.fitbuddy_db);
        SetRepository setRepository = new SQLiteSetRepository(sqLiteOpenHelper);
        exerciseRepository = new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
        workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
    }

    @Test
    public void saveNewWorkout_shouldAssignNewWorkoutId() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.getSets().createSet();
        assertThat(workout.getWorkoutId(), nullValue());

        workoutRepository.save(workout);

        assertThat(workout.getWorkoutId(), any(WorkoutId.class));
    }

    @Test
    public void savePersistedWorkout_shouldKeepWorkoutId() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.getSets().createSet();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        workoutRepository.save(workout);

        assertThat(workout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void saveWorkout_shouldAlsoSaveExercises() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.getSets().createSet();

        workoutRepository.save(workout);

        List<Exercise> loadedExercises = exerciseRepository.allExercisesBelongsTo(workout.getWorkoutId());
        assertThat(loadedExercises.size(), equalTo(1));
        assertThat(loadedExercises.get(0), equalTo(exercise));
    }

    @Test
    public void updateWorkout_shouldSaveTheCorrectEntity() throws Exception {
        WorkoutId workoutId1 = createWorkout("name1");
        WorkoutId workoutId2 = createWorkout("name2");

        Workout workout = workoutRepository.load(workoutId2);
        workout.setName("newname2");
        workoutRepository.save(workout);

        Workout workout1 = workoutRepository.load(workoutId1);
        Workout workout2 = workoutRepository.load(workoutId2);
        assertThat(workout1.getWorkoutId(), equalTo(workoutId1));
        assertThat(workout1.getName(), equalTo("name1"));
        assertThat(workout2.getWorkoutId(), equalTo(workoutId2));
        assertThat(workout2.getName(), equalTo("newname2"));
    }

    @Test(expected = WorkoutException.class)
    public void loadWithNullInstance_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutRepository.load(null);
    }

    @Test(expected = WorkoutException.class)
    public void loadByUnknownWorkoutId_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutRepository.load(new WorkoutId("21"));
    }

    @Test
    public void loadByWorkoutId_shouldReturnWorkoutWithWorkoutId() throws Exception {
        WorkoutId workoutId = createWorkout("a workout");

        Workout loadedWorkout = workoutRepository.load(workoutId);
        assertThat(loadedWorkout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void loadByWorkoutId_shouldReturnWorkoutWithExercises() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise1 = workout.getExercises().createExercise();
        exercise1.getSets().createSet();
        Exercise exercise2 = workout.getExercises().createExercise();
        exercise2.getSets().createSet();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = workoutRepository.load(workoutId);

        assertThat(loadedWorkout.getExercises().size(), equalTo(2));
        assertThat(loadedWorkout.getExercises().get(0), equalTo(exercise1));
        assertThat(loadedWorkout.getExercises().get(1), equalTo(exercise2));
    }

    @Test
    public void deleteExerciseFromWorkout_shouldDeletedFromDatabaseToo() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise1 = workout.getExercises().createExercise();
        exercise1.getSets().createSet();
        Exercise exercise2 = workout.getExercises().createExercise();
        exercise2.getSets().createSet();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();
        Workout persistedWorkout = workoutRepository.load(workoutId);

        persistedWorkout.getExercises().remove(exercise2);
        workoutRepository.save(persistedWorkout);

        Workout loadedWorkout = workoutRepository.load(workoutId);
        assertThat(loadedWorkout.getExercises().size(), equalTo(1));
        assertThat(persistedWorkout.getExercises().get(0), equalTo(exercise1));
    }

    @Test
    public void saveWorkout_shouldUpdateName() throws Exception {
        WorkoutId workoutId = createWorkout("a workout");

        Workout loadedWorkout = workoutRepository.load(workoutId);
        loadedWorkout.setName("new name");
        workoutRepository.save(loadedWorkout);
        Workout reloadedWorkout = workoutRepository.load(workoutId);
        assertThat(reloadedWorkout.getName(), equalTo("new name"));
    }

    @Test
    public void emptyWorkoutList_shouldReturnTheListOfWorkouts() throws Exception {
        List<Workout> workouts = workoutRepository.loadAll();

        assertThat(workouts.size(), equalTo(0));
    }

    @Test
    public void workoutList_shouldReturnTheListOfWorkouts() throws Exception {
        WorkoutId workoutId = createWorkout("workout1");
        WorkoutId workoutId2 = createWorkout("workout2");

        List<Workout> workouts = workoutRepository.loadAll();

        assertThat(workouts.size(), equalTo(2));
        assertThat(workouts.get(0).getWorkoutId(), equalTo(workoutId));
        assertThat(workouts.get(0).getName(), equalTo("workout1"));
        assertThat(workouts.get(1).getWorkoutId(), equalTo(workoutId2));
        assertThat(workouts.get(1).getName(), equalTo("workout2"));
    }

    @Test(expected = WorkoutException.class)
    public void workoutWithoutExercise_shouldThrowExceptionOnSave() throws Exception {
        Workout workout = new BasicWorkout();

        workoutRepository.save(workout);
    }

    @Test
    public void deleteWorkoutWithNull_shouldDoNothing() throws Exception {
        workoutRepository.delete(null);
    }

    @Test(expected = WorkoutException.class)
    public void deleteWorkoutByWorkoutId_shouldRemoveItFromPersistence() throws Exception {
        WorkoutId workoutId = createWorkout("workout1");

        workoutRepository.delete(workoutId);

        workoutRepository.load(workoutId);
    }
}