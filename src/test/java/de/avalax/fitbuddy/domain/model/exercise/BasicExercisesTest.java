package de.avalax.fitbuddy.domain.model.exercise;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicExercisesTest {
    @Test
    public void toString_shouldReturnSetInformation() throws Exception {
        String name = "NameOfExercise";
        Exercise exercise = new BasicExercise();
        exercise.setName(name);

        Exercises exercises = new BasicExercises(Collections.singletonList(exercise));

        assertThat(exercises.toString(), equalTo("BasicExercises [exercises=" +
                "[BasicExercise [name=" + name + "]]]"));
    }
}