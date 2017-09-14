package de.avalax.fitbuddy.presentation.edit.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static org.assertj.core.api.Assertions.assertThat;

public class EditExerciseApplicationServiceTest {
    private EditExerciseApplicationService service;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
        service = new EditExerciseApplicationService();
    }

    @Test
    public void shouldReturnMaxRepsAsTitle() throws Exception {
        assertThat(service.title(aSet().withMaxReps(12).build())).isEqualTo("12");
    }

    @Test
    public void shouldReturnWeightAsSubtitle() throws Exception {
        assertThat(service.subtitle(aSet().build())).isEqualTo("-");
        assertThat(service.subtitle(aSet().withWeight(42.5).build())).isEqualTo("42.5");
        assertThat(service.subtitle(aSet().withWeight(42).build())).isEqualTo("42");
    }
}