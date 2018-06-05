package de.avalax.fitbuddy.presentation.edit.set;

import android.content.Context;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Locale;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.domain.model.set.Set;

import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditSetViewModelTest {

    @Mock
    private Set set;

    private EditSetViewModel editSetViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        editSetViewModel = new EditSetViewModel();
    }

    @Test
    public void onInit_shouldSetSet() {
        editSetViewModel.init(set);

        assertThat(editSetViewModel.getSet().getValue()).isEqualTo(set);
    }

    @Test
    public void updateWeight_shouldSetWeight() {
        editSetViewModel.init(set);

        editSetViewModel.setWeight(21.5);

        verify(set).setWeight(21.5);
    }

    @Test
    public void updateReps_shouldSetMapReps() {
        editSetViewModel.init(set);

        editSetViewModel.setReps(15);

        verify(set).setMaxReps(15);
    }
}