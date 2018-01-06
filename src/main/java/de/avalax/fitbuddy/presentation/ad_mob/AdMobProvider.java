package de.avalax.fitbuddy.presentation.ad_mob;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import de.avalax.fitbuddy.R;

public class AdMobProvider {
    private static final String APP_ID = "ca-app-pub-3067141613739864~9851773284";
    private static final String[] TEST_DEVICES = {
            AdRequest.DEVICE_ID_EMULATOR,
            "8F6B70E5DC92FE9E826BAA77A492D912",
            "84AA29C1F31F798EAC70198A31E9E7A4"
    };

    public void initAdView(Activity activity, View view) {
        MobileAds.initialize(activity, APP_ID);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        for (String testDevice : TEST_DEVICES) {
            adRequest.addTestDevice(testDevice);
        }
        AdView adView = view.findViewById(R.id.adView);
        adView.loadAd(adRequest.build());
    }
}
