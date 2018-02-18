package de.avalax.fitbuddy.runner;

import android.content.Context;
import android.content.SharedPreferences;

import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.port.adapter.service.billing.GooglePlayBillingProvider;
import de.avalax.fitbuddy.presentation.FitbuddyModule;

public class FitbuddyTestModule extends FitbuddyModule {

    private Context context;

    public FitbuddyTestModule(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BillingProvider provideBillingProvider() {
        return new GooglePlayBillingProvider(context, notificationProvider) {
            @Override
            public void sendNotification() {
                SharedPreferences preferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("notification_send", true).apply();
            }
        };
    }
}
