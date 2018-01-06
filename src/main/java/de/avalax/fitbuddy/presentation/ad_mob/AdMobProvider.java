package de.avalax.fitbuddy.presentation.ad_mob;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdMobProvider {
    private static final String APP_ID = "ca-app-pub-3067141613739864~9851773284";
    private static final String[] TEST_DEVICES = {
            AdRequest.DEVICE_ID_EMULATOR,
            "8F6B70E5DC92FE9E826BAA77A492D912",
            "84AA29C1F31F798EAC70198A31E9E7A4"
    };
    private final SharedPreferences preferences;

    public AdMobProvider(Context context) {
        preferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
    }

    public void initAdView(AdView adView) {
        if (isPaid()) {
            adView.setVisibility(View.GONE);
        } else {
            MobileAds.initialize(adView.getContext(), APP_ID);
            AdRequest.Builder adRequest = new AdRequest.Builder();
            for (String testDevice : TEST_DEVICES) {
                adRequest.addTestDevice(testDevice);
            }
            adView.loadAd(adRequest.build());
        }
    }

    public void setPaid() {
        preferences.edit().putBoolean("paid", true).apply();
    }

    private boolean isPaid() {
        return preferences.getBoolean("paid", false);
    }
}
