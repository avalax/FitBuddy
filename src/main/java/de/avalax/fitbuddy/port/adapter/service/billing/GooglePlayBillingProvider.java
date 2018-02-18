package de.avalax.fitbuddy.port.adapter.service.billing;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.HttpURLConnection;

import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.application.billing.NotificationProvider;

public class GooglePlayBillingProvider implements BillingProvider {
    private SharedPreferences preferences;
    private final NotificationProvider notificationProvider;

    public GooglePlayBillingProvider(Context context, NotificationProvider notificationProvider) {
        preferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        this.notificationProvider = notificationProvider;
    }

    @Override
    public boolean isPaid() {
        return preferences.getBoolean("paid", false);
    }

    @Override
    public void purchase() {
        preferences.edit().putBoolean("paid", true).apply();
    }

    @Override
    public int sendNotification() {
        int statusCode = notificationProvider.sendNotification();
        if (statusCode == HttpURLConnection.HTTP_CREATED) {
            preferences.edit().putBoolean("notification_send", true).apply();
        }
        return statusCode;
    }

    @Override
    public boolean hasNotificationSend() {
        return preferences.getBoolean("notification_send", false);
    }
}
