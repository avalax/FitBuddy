package de.avalax.fitbuddy.port.adapter.service.billing;

import android.content.Context;
import android.content.SharedPreferences;

import de.avalax.fitbuddy.application.billing.BillingProvider;

public class GooglePlayBillingProvider implements BillingProvider {
    private SharedPreferences preferences;

    public GooglePlayBillingProvider(Context context) {
        preferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isPaid() {
        return preferences.getBoolean("paid", false);
    }

    @Override
    public void purchase() {
        preferences.edit().putBoolean("paid", true).apply();
    }
}
