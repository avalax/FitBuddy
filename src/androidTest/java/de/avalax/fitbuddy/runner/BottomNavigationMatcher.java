package de.avalax.fitbuddy.runner;

import android.annotation.SuppressLint;
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

    private static Matcher<View> withBottomNavItemCheckedStatus(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            boolean triedMatching;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: " + String.valueOf(isChecked));
                    description.appendText("But was: " + String.valueOf(!isChecked));
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                triedMatching = true;
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }
}
