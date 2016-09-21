package de.avalax.fitbuddy.application.workout;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
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
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml", sdk=21)
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

    private Workout readWorkout() throws Exception {
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
                throw new Exception("could not delete file " + file.getAbsolutePath());
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        workoutSession = new WorkoutSession(context);
    }

    @Test(expected = WorkoutException.class)
    public void noPersistedWorkout_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutSession.getWorkout();
    }

    @Test(expected = WorkoutException.class)
    public void switchedToNullInstance_shouldThrowWorkoutNotFoundExceptiopn() throws Exception {
        workoutSession.switchWorkout(null);

        workoutSession.getWorkout();
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
        workout.createExercise();
        workout.createExercise();
        writeWorkout(workout);
        workoutSession = new WorkoutSession(context);

        Workout persistedWorkout = workoutSession.getWorkout();
        assertThat(persistedWorkout.indexOfCurrentExercise(), equalTo(0));
        persistedWorkout.setCurrentExercise(1);
        workoutSession.saveCurrentWorkout();

        Workout changedPersistedWorkout = readWorkout();
        assertThat(changedPersistedWorkout.indexOfCurrentExercise(), equalTo(1));
        assertThat(changedPersistedWorkout.countOfExercises(), equalTo(2));
    }
}