package de.avalax.fitbuddy.presentation.edit.exercise;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.set.Set;

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
        Set set = aSet().withMaxReps(12).build();
        assertThat(service.title(set)).isEqualTo("12 reps");
        assertThat(service.titleValue(set)).isEqualTo("12");
    }

    @Test
    public void noWeight_shouldReturnDashAsSubtitle() throws Exception {
        Set set = aSet().build();
        assertThat(service.subtitle(set)).isEqualTo("no weight");
        assertThat(service.subtitleValue(set)).isEqualTo("0");
    }

    @Test
    public void doubleAsWeight_shouldReturnWeightAsSubtitle() throws Exception {
        Set set = aSet().withWeight(42.5).build();
        assertThat(service.subtitle(set)).isEqualTo("42.5 kg");
        assertThat(service.subtitleValue(set)).isEqualTo("42.5");
    }

    @Test
    public void integerAsWeight_shouldReturnWeightAsSubtitle() throws Exception {
        Set set = aSet().withWeight(42).build();
        assertThat(service.subtitle(set)).isEqualTo("42 kg");
        assertThat(service.subtitleValue(set)).isEqualTo("42");
    }
}