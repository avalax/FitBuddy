package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import org.fest.assertions.api.ANDROID;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowImageView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.robolectric.Robolectric.shadowOf;

public class Asserts {
    public static void assertOnClickListenerRegistered(ImageView imageView) {
        ShadowImageView shadowTextView = shadowOf(imageView);
        View.OnClickListener onClickListener = shadowTextView.getOnClickListener();
        assertThat(onClickListener,notNullValue(View.OnClickListener.class));
    }

    public static void assertNextStartedActivityForResult(Activity activity, Class cls) {
        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        ShadowActivity.IntentForResult intentForResult = shadowActivity.getNextStartedActivityForResult();
        ANDROID.assertThat(intentForResult.intent).hasComponent(activity, cls);
    }
}
