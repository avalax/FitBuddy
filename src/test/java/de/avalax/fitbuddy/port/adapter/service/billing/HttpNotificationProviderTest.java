package de.avalax.fitbuddy.port.adapter.service.billing;


import org.bouncycastle.asn1.x509.IetfAttrSyntax;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.avalax.fitbuddy.BuildConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HttpNotificationProviderTest {

    @Test
    public void sendNotification_shouldReturnCreatedStatus() throws Exception {
        HttpNotificationProvider provider = new HttpNotificationProvider("unittest");

        int statusCode = provider.sendNotification();

        assertThat(statusCode).isEqualTo(HttpURLConnection.HTTP_CREATED);
    }

    @Test
    public void IOException_shouldReturnUnavailableStatus() throws Exception {
        HttpNotificationProvider provider = new HttpNotificationProvider("unittest") {
            @Override
            protected URL getUrl() throws MalformedURLException {
                throw new MalformedURLException("UnitTest");
            }
        };

        int statusCode = provider.sendNotification();

        assertThat(statusCode).isEqualTo(HttpURLConnection.HTTP_UNAVAILABLE);
    }
}