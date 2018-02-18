package de.avalax.fitbuddy.port.adapter.service.billing;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.HttpURLConnection;

import de.avalax.fitbuddy.BuildConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HttpNotificationProviderTest {

    @Test
    public void sendNotification_shouldReturnCreatedStatus() throws Exception {
        HttpNotificationProvider provider = new HttpNotificationProvider("unittest");

        int statusCode = provider.sendNotification();

        assertThat(statusCode).isEqualTo(HttpURLConnection.HTTP_CREATED);
    }
}