package de.avalax.fitbuddy.app;

import android.view.View;
import android.widget.Button;
import org.robolectric.shadows.ShadowTextView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.robolectric.Robolectric.shadowOf;

public class Asserts {
    public static void assertOnClickListenerRegistered(Button button) {
        ShadowTextView shadowTextView = shadowOf(button);
        View.OnClickListener onClickListener = shadowTextView.getOnClickListener();
        assertThat(onClickListener,notNullValue(View.OnClickListener.class));
    }
}
