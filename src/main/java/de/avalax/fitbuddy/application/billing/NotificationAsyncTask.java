package de.avalax.fitbuddy.application.billing;

import android.os.AsyncTask;

public class NotificationAsyncTask extends AsyncTask<Void, Void, Integer> {
    private BillingProvider billingProvider;
    private NotificationPostExecute notificationPostExecute;

    public NotificationAsyncTask(BillingProvider billingProvider, NotificationPostExecute notificationPostExecute) {
        this.billingProvider = billingProvider;
        this.notificationPostExecute = notificationPostExecute;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return billingProvider.sendNotification();
    }

    @Override
    protected void onPostExecute(Integer result) {
        notificationPostExecute.onPostExecute(result);
    }

    public interface NotificationPostExecute {
        void onPostExecute(int result);
    }
}


