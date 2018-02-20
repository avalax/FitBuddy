package de.avalax.fitbuddy.application.billing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.net.HttpURLConnection;

import de.avalax.fitbuddy.BuildConfig;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NotificationAsyncTaskTest {

    @Test
    public void executeAsyncTask_shouldReturnStatusCodeFromBillingProvider() throws Exception {
        final int[] expectedResult = {-1};
        BillingProvider billingProvider = mock(BillingProvider.class);
        doReturn(HttpURLConnection.HTTP_CREATED).when(billingProvider).sendNotification();
        new NotificationAsyncTask(billingProvider, (result) -> {
            expectedResult[0] = result;
        }).execute();

        ShadowApplication.runBackgroundTasks();

        assertThat(expectedResult).contains(HttpURLConnection.HTTP_CREATED);
    }
}