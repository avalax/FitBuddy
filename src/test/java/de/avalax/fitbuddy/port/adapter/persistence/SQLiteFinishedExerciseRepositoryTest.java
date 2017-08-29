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
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SQLiteFinishedExerciseRepositoryTest {
    private FinishedExerciseRepository finishedExerciseRepository;
    private FinishedWorkoutId finishedWorkoutId;
    private Exercise exercise;

    private void createWorkout(FitbuddySQLiteOpenHelper sqLiteOpenHelper) {
        FinishedWorkoutRepository workoutRepository = new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper, finishedExerciseRepository);
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(new WorkoutId("42"));
        finishedWorkoutId = workoutRepository.saveWorkout(workout);
    }

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        FitbuddySQLiteOpenHelper sqLiteOpenHelper = new FitbuddySQLiteOpenHelper("SQLiteSetRepositoryTest", 1, context, R.raw.fitbuddy_db);
        finishedExerciseRepository = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
        createWorkout(sqLiteOpenHelper);
        exercise = new BasicExercise();
    }

    @Test
    public void saveExerciseWithoutSets_shouldInsertNothing() throws Exception {
        finishedExerciseRepository.save(finishedWorkoutId, exercise);

        List<FinishedExercise> finishedExercises = finishedExerciseRepository.allSetsBelongsTo(finishedWorkoutId);

        assertThat(finishedExercises, emptyCollectionOf(FinishedExercise.class));
    }

    @Test
    public void saveExercise_shouldInsertExerciseWithSetInformation() throws Exception {
        Set set = exercise.getSets().createSet();
        set.setWeight(12.34);
        set.setMaxReps(15);
        set.setReps(12);
        finishedExerciseRepository.save(finishedWorkoutId, exercise);

        List<FinishedExercise> finishedExercises = finishedExerciseRepository.allSetsBelongsTo(finishedWorkoutId);

        assertThat(finishedExercises, hasSize(1));
        FinishedExercise finishedExercise = finishedExercises.get(0);
        assertThat(finishedExercise.getName(), equalTo(exercise.getName()));
        assertThat(finishedExercise.getName(), equalTo(exercise.getName()));
        assertThat(finishedExercise.getWeight(), equalTo(exercise.getSets().get(0).getWeight()));
        assertThat(finishedExercise.getReps(), equalTo(exercise.getSets().get(0).getReps()));
        assertThat(finishedExercise.getMaxReps(), equalTo(exercise.getSets().get(0).getMaxReps()));
    }

    @Test
    public void saveExercise_shouldInsertExerciseWithAllSets() throws Exception {
        exercise.getSets().createSet();
        exercise.getSets().createSet();
        exercise.getSets().createSet();
        finishedExerciseRepository.save(finishedWorkoutId, exercise);

        List<FinishedExercise> finishedExercises = finishedExerciseRepository.allSetsBelongsTo(finishedWorkoutId);

        assertThat(finishedExercises, hasSize(3));
    }
}