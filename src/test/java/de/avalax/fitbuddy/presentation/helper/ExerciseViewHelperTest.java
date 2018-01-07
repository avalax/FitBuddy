package de.avalax.fitbuddy.presentation.helper;

import android.content.Context;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExerciseViewHelperTest {
    private ExerciseViewHelper exerciseViewHelper;

    private Exercise createExerciseWithWeight(double weight) {
        Exercise exercise = new BasicExercise();
        Set set = exercise.getSets().createSet();
        set.setWeight(weight);
        return exercise;
    }

    private Exercise createExerciseWithName(String name) {
        Exercise exercise = new BasicExercise();
        exercise.setName(name);
        return exercise;
    }

    private Exercise createExerciseWithMaxReps(int maxReps) {
        Exercise exercise = new BasicExercise();
        Set set = exercise.getSets().createSet();
        set.setMaxReps(maxReps);
        return exercise;
    }

    private BasicExercise createExerciseWithOneSet() {
        BasicExercise exercise = new BasicExercise();
        exercise.getSets().createSet();
        return exercise;
    }

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        exerciseViewHelper = new ExerciseViewHelper(context, Locale.ENGLISH);
    }

    @Test
    public void testWeightOfExercise() throws Exception {
        assertThat(exerciseViewHelper.weightOfExercise(null), equalTo("no weight"));
        assertThat(exerciseViewHelper.weightOfExercise(new BasicExercise()), equalTo("no weight"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(0.0)), equalTo("no weight"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(10.0)), equalTo("10 kg"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(0.725)), equalTo("0.725 kg"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(-0.725)), equalTo("-0.725 kg"));
    }

    @Test
    public void testMaxRepsOfExercise() throws Exception {
        assertThat(exerciseViewHelper.maxRepsOfExercise(null), equalTo(0));
        assertThat(exerciseViewHelper.maxRepsOfExercise(new BasicExercise()), equalTo(0));
        assertThat(exerciseViewHelper.maxRepsOfExercise(createExerciseWithMaxReps(12)), equalTo(12));
    }

    @Test
    public void testSetCountOfExercise() throws Exception {
        assertThat(exerciseViewHelper.setCountOfExercise(null), equalTo(0));
        assertThat(exerciseViewHelper.setCountOfExercise(new BasicExercise()), equalTo(0));
        assertThat(exerciseViewHelper.setCountOfExercise(createExerciseWithOneSet()), equalTo(1));
    }

    @Test
    public void testExerciseName() throws Exception {
        assertThat(exerciseViewHelper.exerciseName(new BasicExercise()), equalTo("Unknown"));
        assertThat(exerciseViewHelper.exerciseName(createExerciseWithName("my new exercise")), equalTo("my new exercise"));
    }

    @Test
    public void testCutPreviousExerciseName() throws Exception {
        assertThat(exerciseViewHelper.cutPreviousExerciseName(new BasicExercise()), equalTo("known"));
        assertThat(exerciseViewHelper.cutPreviousExerciseName(createExerciseWithName("1234")), equalTo("1234"));
        assertThat(exerciseViewHelper.cutPreviousExerciseName(createExerciseWithName("12345")), equalTo("12345"));
        assertThat(exerciseViewHelper.cutPreviousExerciseName(createExerciseWithName("my new exercise")), equalTo("rcise"));
    }

    @Test
    public void testNextExerciseName() throws Exception {
        assertThat(exerciseViewHelper.cutNextExerciseName(new BasicExercise()), equalTo("Unkno"));
        assertThat(exerciseViewHelper.cutNextExerciseName(createExerciseWithName("1234")), equalTo("1234"));
        assertThat(exerciseViewHelper.cutNextExerciseName(createExerciseWithName("12345")), equalTo("12345"));
        assertThat(exerciseViewHelper.cutNextExerciseName(createExerciseWithName("my new exercise")), equalTo("my ne"));
    }
}