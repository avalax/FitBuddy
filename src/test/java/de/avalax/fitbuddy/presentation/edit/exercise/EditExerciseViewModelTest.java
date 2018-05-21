package de.avalax.fitbuddy.presentation.edit.exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditExerciseViewModelTest {
    @Mock
    private Exercise exercise;

    @Mock
    private EditExerciseViewHelper editExerciseViewHelper;
    private EditExerciseViewModel editExerciseViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn("exercise name").when(exercise).getName();
        editExerciseViewModel = new EditExerciseViewModel();
    }

    @Test
    public void onInit_shouldSetWeightAndReps() {
        editExerciseViewModel.init(null, editExerciseViewHelper, exercise);

        assertThat(editExerciseViewModel.getName()).isEqualTo("exercise name");
        assertThat(editExerciseViewModel.getSetAdapter()).isInstanceOf(SetAdapter.class);
    }

}