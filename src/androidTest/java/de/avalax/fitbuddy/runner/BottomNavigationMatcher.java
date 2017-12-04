package de.avalax.fitbuddy.runner;

import android.support.design.internal.BottomNavigationItemView;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class BottomNavigationMatcher {
    public static Matcher<View> bottomNavItemIsChecked() {
        return withBottomNavItemCheckedStatus(true);
    }

    public static Matcher<View> bottomNavItemIsNotChecked() {
        return withBottomNavItemCheckedStatus(false);
    }

    public static Matcher<View> bottomNavItemIsCheckable() {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with BottomNavigationItem check disabled status");
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                return item.getItemData().isCheckable();
            }
        };
    }

    private static Matcher<View> withBottomNavItemCheckedStatus(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with BottomNavigationItem check status: " + String.valueOf(isChecked));
                description.appendText("But was: " + String.valueOf(!isChecked));
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }
}
