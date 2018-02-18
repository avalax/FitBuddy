package de.avalax.fitbuddy.application.billing;

public interface BillingProvider {
    boolean isPaid();
    void purchase();
    int sendNotification();
    boolean hasNotificationSend();
}
