package de.avalax.fitbuddy.runner;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class BillingRunner {
    private FitbuddyActivityTestRule activityRule;

    public BillingRunner(FitbuddyActivityTestRule activityRule) {
        this.activityRule = activityRule;
    }

    public void hasNotificationSend() {
        assertThat(activityRule.billingProvider.hasNotificationSend(), equalTo(true));
    }
}
