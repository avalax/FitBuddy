package de.avalax.fitbuddy.application.workout;

import android.content.Context;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
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

    private Workout readWorkout() throws IOException {
        Workout workout;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            workout = (Workout) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            workout = null;
        }
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
        context = Robolectric.application.getApplicationContext();
        workoutSession = new WorkoutSession(context);
    }

    @Test(expected = WorkoutNotFoundException.class)
    public void noPersistedWorkout_shouldThrowWorkoutNotFoundException() throws Exception {
        workoutSession.getWorkout();
    }

    @Test(expected = WorkoutNotFoundException.class)
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
        assertThat(changedPersistedWorkout.getExercises(), hasSize(2));
    }
}