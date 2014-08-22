package de.avalax.fitbuddy.presentation.helper;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExerciseViewHelperTest {
    private ExerciseViewHelper exerciseViewHelper;

    private BasicExercise createExerciseWithWeight(double weight) {
        BasicExercise exercise = new BasicExercise();
        BasicSet set = new BasicSet();
        set.setWeight(weight);
        exercise.addSet(set);
        return exercise;
    }

    private BasicExercise createExerciseWithName(String name) {
        BasicExercise exercise = new BasicExercise();
        exercise.setName(name);
        return exercise;
    }

    private Exercise createExerciseWithMaxReps(int maxReps) {
        Exercise exercise = new BasicExercise();
        Set set = new BasicSet();
        set.setMaxReps(maxReps);
        exercise.addSet(set);
        return exercise;
    }

    private BasicExercise createExerciseWithOneSet() {
        BasicExercise exercise = new BasicExercise();
        exercise.addSet(new BasicSet());
        return exercise;
    }

    @Before
    public void setUp() throws Exception {
        exerciseViewHelper = new ExerciseViewHelper(Locale.ENGLISH);
    }

    @Test
    public void testWeightOfExercise() throws Exception {
        assertThat(exerciseViewHelper.weightOfExercise(null, -1), equalTo("-"));
        assertThat(exerciseViewHelper.weightOfExercise(new BasicExercise(), 0), equalTo("-"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(0.0), 0), equalTo("-"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(10.0), 0), equalTo("10"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(0.725), 0), equalTo("0.725"));
        assertThat(exerciseViewHelper.weightOfExercise(createExerciseWithWeight(-0.725), 0), equalTo("-0.725"));
    }

    @Test
    public void testNameOfExercise() throws Exception {
        assertThat(exerciseViewHelper.nameOfExercise(null), equalTo("unnamed exercise"));
        assertThat(exerciseViewHelper.nameOfExercise(new BasicExercise()), equalTo("unnamed exercise"));
        assertThat(exerciseViewHelper.nameOfExercise(createExerciseWithName("my new exercise")), equalTo("my new exercise"));
    }

    @Test
    public void testMaxRepsOfExercise() throws Exception {
        assertThat(exerciseViewHelper.maxRepsOfExercise(null, -1), equalTo(0));
        assertThat(exerciseViewHelper.maxRepsOfExercise(new BasicExercise(), 0), equalTo(0));
        assertThat(exerciseViewHelper.maxRepsOfExercise(createExerciseWithMaxReps(12), 0), equalTo(12));
    }

    @Test
    public void testSetCountOfExercise() throws Exception {
        assertThat(exerciseViewHelper.setCountOfExercise(null), equalTo(0));
        assertThat(exerciseViewHelper.setCountOfExercise(new BasicExercise()), equalTo(0));
        assertThat(exerciseViewHelper.setCountOfExercise(createExerciseWithOneSet()), equalTo(1));
    }
}