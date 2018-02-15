package de.avalax.fitbuddy.port.adapter.service.billing;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.avalax.fitbuddy.BuildConfig;
import de.avalax.fitbuddy.application.billing.BillingProvider;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GooglePlayBillingProviderTest {

    private BillingProvider billingProvider;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("fitbuddy", Context.MODE_PRIVATE);
        billingProvider = new GooglePlayBillingProvider(context);
    }

    @Test
    public void initialStart_shouldNotBePaid() throws Exception {
        assertThat(billingProvider.isPaid()).isFalse();
    }

    @Test
    public void alreadyPaid_shouldBePaid() throws Exception {
        sharedPreferences.edit().putBoolean("paid", true).commit();

        assertThat(billingProvider.isPaid()).isTrue();
    }

    @Test
    public void onPayment_shouldBePaid() throws Exception {
        billingProvider.purchase();

        assertThat(billingProvider.isPaid()).isTrue();
    }
}