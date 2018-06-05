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
import static de.avalax.fitbuddy.presentation.edit.set.SetBindingAdapter.setRepsFromSet;
import static de.avalax.fitbuddy.presentation.edit.set.SetBindingAdapter.setWeightFromSet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SetBindingAdapterTest {

    @Mock
    private TextView textView;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        MockitoAnnotations.initMocks(this);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        doReturn(context).when(textView).getContext();
    }

    @Test
    public void shouldReturnMaxRepsAsTitle() throws Exception {
        Set set = aSet().withMaxReps(12).build();

        setRepsFromSet(textView, set);

        verify(textView).setText("12 reps");
    }

    @Test
    public void noWeight_shouldReturnDashAsSubtitle() throws Exception {
        Set set = aSet().build();

        setWeightFromSet(textView, set);

        verify(textView).setText("no weight");
    }

    @Test
    public void doubleWithOneDigitAsWeight_shouldReturnWeightAsSubtitle() throws Exception {
        Set set = aSet().withWeight(42.5).build();

        setWeightFromSet(textView, set);

        verify(textView).setText("42.5 kg");
    }

    @Test
    public void doubleAsWeight_shouldReturnWeightAsSubtitle() throws Exception {
        Set set = aSet().withWeight(42.625).build();

        setWeightFromSet(textView, set);

        verify(textView).setText("42.625 kg");
    }

    @Test
    public void integerAsWeight_shouldReturnWeightAsSubtitle() throws Exception {
        Set set = aSet().withWeight(42).build();

        setWeightFromSet(textView, set);

        verify(textView).setText("42 kg");
    }
}