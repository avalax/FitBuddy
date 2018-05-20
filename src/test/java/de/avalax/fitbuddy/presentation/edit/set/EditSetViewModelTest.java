package de.avalax.fitbuddy.presentation.edit.set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditSetViewModelTest {

    @Mock
    private Set set;

    @Mock
    private EditExerciseViewHelper editExerciseViewHelper;
    private EditSetViewModel editSetViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn("42.0 kg").when(editExerciseViewHelper).weightFrom(set);
        doReturn("12 reps").when(editExerciseViewHelper).repsFrom(set);
        editSetViewModel = new EditSetViewModel();
    }

    @Test
    public void onInit_shouldSetWeightAndReps() {
        editSetViewModel.init(editExerciseViewHelper, set);

        assertThat(editSetViewModel.getWeight().getValue()).isEqualTo("42.0 kg");
        assertThat(editSetViewModel.getReps().getValue()).isEqualTo("12 reps");
    }

    @Test
    public void updateWeight_shouldSetWeight() {
        editSetViewModel.init(editExerciseViewHelper, set);

        editSetViewModel.setWeight(21.5);

        verify(editSetViewModel.getSet()).setWeight(21.5);
    }

    @Test
    public void updateReps_shouldSetMapReps() {
        editSetViewModel.init(editExerciseViewHelper, set);

        editSetViewModel.setReps(15);

        verify(editSetViewModel.getSet()).setMaxReps(15);
    }
}