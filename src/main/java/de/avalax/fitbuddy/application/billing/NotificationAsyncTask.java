package de.avalax.fitbuddy.application.billing;

import android.os.AsyncTask;

import java.net.HttpURLConnection;

public class NotificationAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BillingProvider billingProvider;
    private NotificationPostExecute notificationPostExecute;

    public NotificationAsyncTask(BillingProvider billingProvider, NotificationPostExecute notificationPostExecute) {
        super();
        this.billingProvider = billingProvider;
        this.notificationPostExecute = notificationPostExecute;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        if (billingProvider.hasNotificationSend()) {
            return HttpURLConnection.HTTP_CREATED;
        } else {
            return billingProvider.sendNotification();
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        notificationPostExecute.onPostExecute(result);
    }

    public interface NotificationPostExecute {
        void onPostExecute(int result);
    }
}


