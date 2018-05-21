package de.avalax.fitbuddy.port.adapter.service.billing;

import android.util.Base64;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.avalax.fitbuddy.application.billing.NotificationProvider;

public class HttpNotificationProvider implements NotificationProvider {
    private String appName;

    public HttpNotificationProvider(String appName) {
        this.appName = appName;
    }

    @Override
    public int sendNotification() {
        try {
            URL url = getUrl();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + getAuth());

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                String params = "name=" + appName;
                wr.write(params.getBytes("UTF-8"));
            }
            return connection.getResponseCode();
        } catch (IOException e) {
            return HttpURLConnection.HTTP_UNAVAILABLE;
        }
    }

    private String getAuth() throws UnsupportedEncodingException {
        String userPassword = appName + ":" + appName;
        return Base64.encodeToString(userPassword.getBytes("UTF-8"), Base64.NO_WRAP);
    }

    protected URL getUrl() throws MalformedURLException {
        return new URL("http://silex.avalax.de/insights");
    }
}
