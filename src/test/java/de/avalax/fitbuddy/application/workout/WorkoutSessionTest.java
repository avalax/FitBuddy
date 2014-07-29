package de.avalax.fitbuddy.application.workout;

import android.content.Context;
import android.content.SharedPreferences;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class WorkoutSessionTest {
    private WorkoutSession workoutSession;

    private SharedPreferences sharedPreferences;
    private Context context;
    private File file;
    private File file2;

    private void writeSelectedSets(Map<ExerciseId, Integer> selectedSets) throws IOException {
        file = new File(context.getDir("data", Context.MODE_PRIVATE), "selectedSets");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(selectedSets);
        outputStream.flush();
        outputStream.close();
    }

    private Map<ExerciseId, Integer> readSelectedSets() throws IOException {
        Map<ExerciseId, Integer> selectedSets;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "selectedSets");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            selectedSets = (Map<ExerciseId, Integer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            selectedSets = new HashMap<>();
        }
        return selectedSets;
    }

    private void writeRepsForSets(Map<SetId, Integer> selectedSets) throws IOException {
        file2 = new File(context.getDir("data", Context.MODE_PRIVATE), "repsForSet");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file2));
        outputStream.writeObject(selectedSets);
        outputStream.flush();
        outputStream.close();
    }

    private Map<SetId, Integer> readRepsForSets() throws IOException {
        Map<SetId, Integer> selectedSets;
        File file2 = new File(context.getDir("data", Context.MODE_PRIVATE), "repsForSet");
        try {
            FileInputStream fis = new FileInputStream(file2);
            ObjectInputStream ois = new ObjectInputStream(fis);
            selectedSets = (Map<SetId, Integer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            selectedSets = new HashMap<>();
        }
        return selectedSets;
    }

    @After
    public void tearDown() throws Exception {
        if (file != null) {
            if (!file.delete()) {
                throw new Exception("could not delete file " + file.getAbsolutePath());
            }
        }
        if (file2 != null) {
            if (!file2.delete()) {
                throw new Exception("could not delete file " + file2.getAbsolutePath());
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        String lastWorkoutId = "21";
        context = Robolectric.application.getApplicationContext();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, lastWorkoutId).commit();
        workoutSession = new WorkoutSession(sharedPreferences, context);
    }

    @Test
    public void switchWorkout_shouldLoadAndSetWorkoutFromRepository() throws Exception {
        WorkoutId workoutId = new WorkoutId("42");

        workoutSession.switchWorkoutById(workoutId);

        assertThat(sharedPreferences.getString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, "-1"), equalTo(workoutId.id()));
    }

    @Test
    public void noPersistedRepsForSets_shouldReturnDefault() throws Exception {
        SetId setId = new SetId("42");

        Integer reps = workoutSession.repsForSet(setId);

        assertThat(reps, equalTo(0));
    }

    @Test
    public void noRepsForSet_shouldReturnDefault() throws Exception {
        Map<SetId, Integer> repsForSets = new HashMap<>();
        writeRepsForSets(repsForSets);
        SetId setId = new SetId("42");

        Integer reps = workoutSession.repsForSet(setId);

        assertThat(reps, equalTo(0));
    }

    @Test
    public void persistedRepsOfSet_shouldReturnReps() throws Exception {
        SetId setId = new SetId("42");
        int expectedReps = 12;
        Map<SetId, Integer> repsForSets = new HashMap<>();
        repsForSets.put(setId, expectedReps);
        writeRepsForSets(repsForSets);
        workoutSession = new WorkoutSession(sharedPreferences, context);

        Integer reps = workoutSession.repsForSet(setId);

        assertThat(reps, equalTo(expectedReps));
    }

    @Test
    public void setRepsForSet_shouldPersistChange() throws Exception {
        SetId setId = new SetId("42");
        int expectedReps = 12;
        workoutSession.setRepsOfSet(setId, expectedReps);

        Map<SetId, Integer> repsForSet = readRepsForSets();

        assertThat(repsForSet.get(setId), equalTo(expectedReps));
    }

    @Test
    public void setRepsForSet_shouldReturnReps() throws Exception {
        SetId setId = new SetId("42");
        int expectedReps = 12;
        workoutSession.setRepsOfSet(setId, expectedReps);

        Integer reps = workoutSession.repsForSet(setId);

        assertThat(reps, equalTo(expectedReps));
    }

    @Test
    public void noPersistedSelectedSets_shouldReturnDefault() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");

        Integer position = workoutSession.selectedSetOfExercise(exerciseId);

        assertThat(position, equalTo(0));
    }

    @Test
    public void noSelectedSet_shouldReturnDefault() throws Exception {
        Map<ExerciseId, Integer> selectedSets = new HashMap<>();
        writeSelectedSets(selectedSets);
        ExerciseId exerciseId = new ExerciseId("42");

        Integer position = workoutSession.selectedSetOfExercise(exerciseId);

        assertThat(position, equalTo(0));
    }

    @Test
    public void selectedSet_shouldReturnPosition() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        int expectedPosition = 12;
        Map<ExerciseId, Integer> selectedSets = new HashMap<>();
        selectedSets.put(exerciseId, expectedPosition);
        writeSelectedSets(selectedSets);
        workoutSession = new WorkoutSession(sharedPreferences, context);

        Integer position = workoutSession.selectedSetOfExercise(exerciseId);

        assertThat(expectedPosition, equalTo(position));
    }

    @Test
    public void setSetPosition_shouldPersistChange() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        int expectedPosition = 12;
        workoutSession.setSelectedSetOfExercise(exerciseId, expectedPosition);

        Map<ExerciseId, Integer> selectedSets = readSelectedSets();

        assertThat(selectedSets.get(exerciseId), equalTo(expectedPosition));
    }

    @Test
    public void setSetPosition_shouldReturnSetPosition() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        int expectedPosition = 12;
        workoutSession.setSelectedSetOfExercise(exerciseId, expectedPosition);

        Integer position = workoutSession.selectedSetOfExercise(exerciseId);

        assertThat(position, equalTo(expectedPosition));
    }

    @Test
    public void selectedExerciseOnInit_shouldReturnDefault() throws Exception {
       assertThat(workoutSession.selectedExercise(), equalTo(0));
    }

    @Test
    public void selectedExercise_shouldReturnFromSharedPreferences() throws Exception {
        sharedPreferences.edit().putInt("selectedExercise", 42).commit();
        assertThat(workoutSession.selectedExercise(), equalTo(42));
    }

    @Test
    public void setSelectedExercise_shouldSaveToSharedPreferences() throws Exception {
        workoutSession.setSelectedExercise(21);
        assertThat(sharedPreferences.getInt("selectedExercise",0), equalTo(21));
    }
}