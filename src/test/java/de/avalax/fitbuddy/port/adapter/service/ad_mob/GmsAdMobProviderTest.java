package de.avalax.fitbuddy.port.adapter.service.ad_mob;

import android.content.Context;
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
import de.avalax.fitbuddy.application.billing.BillingProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GmsAdMobProviderTest {

    private AdMobProvider adMobProvider;
    private BillingProvider billingProvider;
    private AdView adView;

    @Before
    public void setUp() throws Exception {
        billingProvider = mock(BillingProvider.class);
        adMobProvider = new GmsAdMobProvider(billingProvider);
        Context context = RuntimeEnvironment.application.getApplicationContext();
        adMobProvider = new GmsAdMobProvider(billingProvider);
        adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }

    @Test
    public void initialStart_shouldBeVisible() throws Exception {
        adMobProvider.initAdView(adView);

        assertThat(adView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void alreadyPaid_shouldBeHidden() throws Exception {
        doReturn(true).when(billingProvider).isPaid();

        adMobProvider.initAdView(adView);

        assertThat(adView.getVisibility()).isEqualTo(View.GONE);
    }
}