package de.avalax.fitbuddy.port.adapter.persistence;

import android.content.Context;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteWorkoutRepositoryTest {

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private WorkoutId createWorkout(String name) {
        Workout workout = new BasicWorkout();
        workout.setName(name);
        workoutRepository.save(workout);
        return workout.getWorkoutId();
    }

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteWorkoutRepositoryTest", 1, context, R.raw.fitbuddy_db);
        SetRepository setRepository = new SQLiteSetRepository(sqLiteOpenHelper);
        exerciseRepository = new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
        workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
    }

    @Test
    public void saveUnpersistedWorkout_shouldAssignNewWorkoutId() throws Exception {
        Workout workout = new BasicWorkout();

        assertThat(workout.getWorkoutId(), nullValue());
        workoutRepository.save(workout);
        assertThat(workout.getWorkoutId(), any(WorkoutId.class));
    }

    @Test
    public void savePersistedWorkout_shouldKeepWorkoutId() throws Exception {
        Workout workout = new BasicWorkout();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        workoutRepository.save(workout);
        assertThat(workout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void saveWorkout_shouldAlsoSaveExercises() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise = workout.createExercise(0);

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

    @Test(expected = WorkoutNotFoundException.class)
    public void loadWithNullInstance_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutRepository.load(null);
    }

    @Test(expected = WorkoutNotFoundException.class)
    public void loadByUnknownWorkoutId_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutRepository.load(new WorkoutId("21"));
    }

    @Test
    public void loadByWorkoutId_shouldReturnWorkoutWithWorkoutId() throws Exception {
        Workout workout = new BasicWorkout();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = workoutRepository.load(workoutId);
        assertThat(loadedWorkout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void loadByWorkoutId_shouldReturnWorkoutWithExercises() throws Exception {
        Workout workout = new BasicWorkout();
        Exercise exercise1 = workout.createExercise(0);
        Exercise exercise2 = workout.createExercise(1);
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = workoutRepository.load(workoutId);

        assertThat(loadedWorkout.countOfExercises(), equalTo(2));
        assertThat(loadedWorkout.exerciseAtPosition(0), equalTo(exercise1));
        assertThat(loadedWorkout.exerciseAtPosition(1), equalTo(exercise2));
    }

    @Test
    public void saveWorkout_shouldUpdateName() throws Exception {
        Workout workout = new BasicWorkout();
        workoutRepository.save(workout);
        WorkoutId workoutId = workout.getWorkoutId();

        Workout loadedWorkout = workoutRepository.load(workoutId);
        loadedWorkout.setName("new name");
        workoutRepository.save(loadedWorkout);
        Workout reloadedWorkout = workoutRepository.load(workoutId);
        assertThat(reloadedWorkout.getName(), equalTo("new name"));
    }

    @Test
    public void emptyWorkoutList_shouldReturnTheListOfWorkouts() throws Exception {
        List<WorkoutListEntry> workoutList = workoutRepository.getWorkoutList();

        assertThat(workoutList.size(), equalTo(0));
    }

    @Test
    public void workoutList_shouldReturnTheListOfWorkouts() throws Exception {
        WorkoutId workoutId = createWorkout("workout1");
        WorkoutId workoutId2 = createWorkout("workout2");

        List<WorkoutListEntry> workoutList = workoutRepository.getWorkoutList();

        assertThat(workoutList.size(), equalTo(2));
        assertThat(workoutList.get(0).getWorkoutId(), equalTo(workoutId));
        assertThat(workoutList.get(0).toString(), equalTo("workout1"));
        assertThat(workoutList.get(1).getWorkoutId(), equalTo(workoutId2));
        assertThat(workoutList.get(1).toString(), equalTo("workout2"));
    }

    @Test
    public void deleteWorkoutWithNull_shouldDoNothing() throws Exception {
        workoutRepository.delete(null);
    }

    @Test(expected = WorkoutNotFoundException.class)
    public void deleteWorkoutByWorkoutId_shouldRemoveItFromPersistence() throws Exception {
        WorkoutId workoutId = createWorkout("workout1");

        workoutRepository.delete(workoutId);

        workoutRepository.load(workoutId);
    }
}