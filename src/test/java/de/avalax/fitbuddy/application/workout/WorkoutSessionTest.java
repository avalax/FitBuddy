package de.avalax.fitbuddy.application.workout;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WorkoutSessionTest {
    private WorkoutSession workoutSession;
    private Context context;
    private File file;

    private void writeWorkout(Workout workout) throws IOException {
        file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(workout);
        outputStream.flush();
        outputStream.close();
    }

    private Workout readWorkout() throws IOException, ClassNotFoundException {
        Workout workout;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        workout = (Workout) ois.readObject();
        ois.close();
        fis.close();
        return workout;
    }

    @After
    public void tearDown() throws Exception {
        if (file != null) {
            if (!file.delete()) {
                throw new Exception("could not remove file " + file.getAbsolutePath());
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        workoutSession = new WorkoutSession(context);
    }

    @Test
    public void noPersistedWorkout_shouldReturnNull() throws Exception {
        assertThat(workoutSession.getWorkout(), nullValue());
    }

    @Test
    public void noPersistedWorkout_shouldReturnFalse() throws Exception {
        boolean hasWorkout = workoutSession.hasWorkout();

        assertThat(hasWorkout, equalTo(false));
    }

    @Test
    public void switchedToNullInstance_shouldReturnNull() throws Exception {
        workoutSession.switchWorkout(null);

        assertThat(workoutSession.getWorkout(), nullValue());
    }

    @Test
    public void switchWorkout_shouldLoadWorkoutFromFile() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);

        workoutSession.switchWorkout(workout);

        assertThat(workoutSession.getWorkout().getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void switchWorkout_shouldHaveWorkout() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);

        workoutSession.switchWorkout(workout);

        assertThat(workoutSession.hasWorkout(), equalTo(true));
    }

    @Test(expected = WorkoutException.class)
    public void switchWorkout_shouldThrowWorkoutExceptionWhenIOExceptionOccurs() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);
        context = RuntimeEnvironment.application.getApplicationContext();
        workoutSession = new WorkoutSession(context) {
            @Override
            protected void writeCurrentWorkoutToFile() throws IOException {
                throw new IOException();
            }
        };

        workoutSession.switchWorkout(workout);
    }

    @Test(expected = WorkoutException.class)
    public void saveWorkout_shouldThrowWorkoutExceptionWhenIOExceptionOccurs() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        workoutSession = new WorkoutSession(context) {
            @Override
            protected void writeCurrentWorkoutToFile() throws IOException {
                throw new IOException();
            }
        };

        workoutSession.saveCurrentWorkout();
    }

    @Test
    public void persistedWorkout_shouldReturnWorkoutWithId() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);
        writeWorkout(workout);
        workoutSession = new WorkoutSession(context);

        Workout persistedWorkout = workoutSession.getWorkout();

        assertThat(persistedWorkout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void saveWorkoutWithChanges_shouldReturnWorkoutWithChanges() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");
        Workout workout = new BasicWorkout();
        workout.setWorkoutId(workoutId);
        workout.getExercises().createExercise();
        workout.getExercises().createExercise();
        writeWorkout(workout);
        workoutSession = new WorkoutSession(context);

        Workout persistedWorkout = workoutSession.getWorkout();
        assertThat(persistedWorkout.getExercises().indexOfCurrentExercise(), equalTo(0));
        persistedWorkout.getExercises().setCurrentExercise(1);
        workoutSession.saveCurrentWorkout();

        Workout changedPersistedWorkout = readWorkout();
        assertThat(changedPersistedWorkout.getExercises().indexOfCurrentExercise(), equalTo(1));
        assertThat(changedPersistedWorkout.getExercises().size(), equalTo(2));
    }
}