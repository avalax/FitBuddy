package de.avalax.fitbuddy.port.adapter.service.ad_mob;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.application.ad_mod.AdMobProvider;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AdMobProviderTest {

    private AdMobProvider adMobProvider;
    private SharedPreferences sharedPreferences;
    private AdView adView;

    @Before
    public void setUp() throws Exception {
        sharedPreferences = RuntimeEnvironment.application.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        adMobProvider = new GmsAdMobProvider(context);
        adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }

    @Test
    public void initialStart_shouldNotBePaid() throws Exception {
        adMobProvider.initAdView(adView);

        assertThat(adView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void alreadyPaid_shouldBePaid() throws Exception {
        sharedPreferences.edit().putBoolean("paid", true).commit();

        adMobProvider.initAdView(adView);

        assertThat(adView.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void onPayment_shouldBePaid() throws Exception {
        adMobProvider.setPaid();
        adMobProvider.initAdView(adView);

        assertThat(adView.getVisibility()).isEqualTo(View.GONE);
        assertThat(sharedPreferences.getBoolean("paid", false)).isTrue();
    }
}