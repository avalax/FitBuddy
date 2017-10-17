package de.avalax.fitbuddy.presentation.edit.exercise;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;

import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditExerciseViewHelperTest {
    private EditExerciseViewHelper service;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        service = new EditExerciseViewHelper(context);
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