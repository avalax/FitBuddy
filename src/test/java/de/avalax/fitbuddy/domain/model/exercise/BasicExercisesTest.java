package de.avalax.fitbuddy.domain.model.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasicExercisesTest {
    @Test
    public void toString_shouldReturnExercisesInformation() throws Exception {
        String name = "NameOfExercise";
        Exercise exercise = new BasicExercise();
        exercise.setName(name);

        Exercises exercises = new BasicExercises(Collections.singletonList(exercise));

        assertThat(exercises.toString()).isEqualTo("BasicExercises [exercises=" +
                "[BasicExercise [name=" + name + "]]]");
    }

    @Test
    public void addExercise_shouldBeAddedToExercises() throws Exception {
        Exercise exercise = anExercise().withName("exercise to add").build();
        Exercises exercises = new BasicExercises(new ArrayList<>());

        exercises.add(exercise);

        assertThat(exercises).containsOnly(exercise);
    }

    @Test
    public void setExercise_shouldReplaceExerciseAtPosition() throws Exception {
        Exercise exercise = anExercise().withName("old exercise").build();
        Exercise newExercise = anExercise().withName("new exercise").build();
        Exercises exercises = new BasicExercises(new ArrayList<>());
        exercises.add(exercise);

        exercises.set(0, newExercise);

        assertThat(exercises).containsOnly(newExercise);
    }
}