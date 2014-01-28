package de.avalax.fitbuddy.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import org.fest.assertions.api.ANDROID;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
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

    public static void assertNextStartedActivityForResult(Fragment fragment, Class cls) {
        FragmentActivity activity = fragment.getActivity();
        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        ShadowActivity.IntentForResult intentForResult = shadowActivity.getNextStartedActivityForResult();
        ANDROID.assertThat(intentForResult.intent).hasComponent(activity, cls);
    }
}
