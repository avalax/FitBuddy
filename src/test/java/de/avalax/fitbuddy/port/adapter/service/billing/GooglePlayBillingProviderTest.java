package de.avalax.fitbuddy.port.adapter.service.billing;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.HttpURLConnection;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.application.billing.NotificationProvider;

import static java.net.HttpURLConnection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GooglePlayBillingProviderTest {

    private BillingProvider billingProvider;
    private SharedPreferences sharedPreferences;
    private NotificationProvider notificationProvider;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        notificationProvider = mock(NotificationProvider.class);
        billingProvider = new GooglePlayBillingProvider(context, notificationProvider);
    }

    @Test
    public void initialStart_shouldNotBePaid() throws Exception {
        assertThat(billingProvider.isPaid()).isFalse();
    }

    @Test
    public void alreadyPaid_shouldReturnTrue() throws Exception {
        sharedPreferences.edit().putBoolean("paid", true).commit();

        assertThat(billingProvider.isPaid()).isTrue();
    }

    @Test
    public void onPayment_shouldBePaid() throws Exception {
        billingProvider.purchase();

        assertThat(billingProvider.isPaid()).isTrue();
    }

    @Test
    public void initialStart_shouldNotBeNotified() throws Exception {
        assertThat(billingProvider.hasNotificationSend()).isFalse();
    }

    @Test
    public void alreadyNotified_shouldReturnTrue() throws Exception {
        sharedPreferences.edit().putBoolean("notification_send", true).commit();

        assertThat(billingProvider.hasNotificationSend()).isTrue();
    }

    @Test
    public void sendNotification_shouldReturnCreatedStatusCode() throws Exception {
        doReturn(HTTP_CREATED).when(notificationProvider).sendNotification();

        int statusCode = billingProvider.sendNotification();

        assertThat(statusCode).isEqualTo(HTTP_CREATED);
        assertThat(billingProvider.hasNotificationSend()).isTrue();
    }

    @Test
    public void failedSendNotification_shouldReturnCreatedStatusCode() throws Exception {
        doReturn(HTTP_UNAVAILABLE).when(notificationProvider).sendNotification();

        int statusCode = billingProvider.sendNotification();

        assertThat(statusCode).isEqualTo(HTTP_UNAVAILABLE);
        assertThat(billingProvider.hasNotificationSend()).isFalse();
    }
}