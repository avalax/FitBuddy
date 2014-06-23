package de.avalax.fitbuddy.port.adapter.persistence;

import android.app.Activity;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteExerciseRepositoryTest {

    private ExerciseRepository exerciseRepository;

    private SetRepository setRepository;

    private WorkoutId workoutId;

    private ExerciseId createExercise(String name) {
        Exercise exercise = new BasicExercise(name, new ArrayList<Set>());
        exerciseRepository.save(workoutId, exercise);
        return exercise.getExerciseId();
    }

    private void createWorkout(FitbuddySQLiteOpenHelper sqLiteOpenHelper) {
        WorkoutRepository workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
        Workout workout = new BasicWorkout(new LinkedList<Exercise>());
        workoutRepository.save(workout);
        workoutId = workout.getWorkoutId();
    }

    @Before
    public void setUp() throws Exception {
        Activity activity = Robolectric.buildActivity(ManageWorkoutActivity.class).create().get();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteExerciseRepositoryTest", 1, activity, R.raw.fitbuddy_db);
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
        Exercise exercise = new BasicExercise("name", new ArrayList<Set>());
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
        Exercise exercise = new BasicExercise("name", sets);

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
        ExerciseId exerciseId = createExercise("name");

        Exercise loadExercise = exerciseRepository.load(exerciseId);
        assertThat(loadExercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void loadByExerciseId_shouldReturnExerciseWithSets() {
        ArrayList<Set> sets = new ArrayList<>();
        Set set1 = new BasicSet(42, 12);
        sets.add(set1);
        Set set2 = new BasicSet(40, 10);
        sets.add(set2);
        Exercise exercise = new BasicExercise("name", sets);

        exerciseRepository.save(workoutId, exercise);
        when(setRepository.allSetsBelongsTo(exercise.getExerciseId())).thenReturn(sets);
        ExerciseId exerciseId = exercise.getExerciseId();

        Exercise loadedExercise = exerciseRepository.load(exerciseId);
        assertThat(loadedExercise.getSets().size(), equalTo(2));
        assertThat(loadedExercise.getSets().get(0), equalTo(set1));
        assertThat(loadedExercise.getSets().get(1), equalTo(set2));
    }

    @Test
    public void loadAllExercisesBelongsTo_shouldReturnExercisesOfWorkout() throws Exception {
        ExerciseId exerciseId1 = createExercise("name");
        ExerciseId exerciseId2 = createExercise("name");

        LinkedList<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);

        assertThat(exercises.size(), equalTo(2));
        assertThat(exercises.get(0).getExerciseId(), equalTo(exerciseId1));
        assertThat(exercises.get(1).getExerciseId(), equalTo(exerciseId2));
    }

    @Test
    public void loadAllExercisesBelongsTo_shouldAddSetsToExercise() throws Exception {
        ArrayList<Set> sets = new ArrayList<>();
        Set set1 = new BasicSet(42, 12);
        sets.add(set1);
        Set set2 = new BasicSet(40, 10);
        sets.add(set2);
        Exercise exercise = new BasicExercise("name", sets);

        exerciseRepository.save(workoutId, exercise);
        when(setRepository.allSetsBelongsTo(exercise.getExerciseId())).thenReturn(sets);

        LinkedList<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(workoutId);
        assertThat(exercises.get(0).getSets().size(), equalTo(2));
        assertThat(exercises.get(0).getSets().get(0), equalTo(set1));
        assertThat(exercises.get(0).getSets().get(1), equalTo(set2));
    }

    @Test
    public void deleteExerciseByExerciseId_shouldRemoveItFromPersistence() throws Exception {
        ExerciseId exerciseId = createExercise("name");

        exerciseRepository.delete(exerciseId);

        assertThat(exerciseRepository.load(exerciseId), nullValue());
    }
}