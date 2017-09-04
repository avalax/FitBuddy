package de.avalax.fitbuddy;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.runner.FitbuddyActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsChecked;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsNotChecked;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FitbuddyAcceptanceTest {

    /**
     * private int actionBarTitleId() {
     * Resources resources = activityRule.getActivity().getResources();
     * return resources.getIdentifier("action_bar_title", "id", "android");
     * }
     **/

    @Rule
    public FitbuddyActivityTestRule activityRule = new FitbuddyActivityTestRule(
            MainActivity.class);

    @Test
    public void initialStart_shouldShowEmptyStartFragment() throws Exception {
        onView(withId(R.id.navigation_start_item)).check(matches(bottomNavItemIsChecked()));
        onView(withId(R.id.navigation_workout_item)).check(matches(bottomNavItemIsNotChecked()));
        onView(withId(R.id.navigation_statistics_item)).check(matches(bottomNavItemIsNotChecked()));
        onView(withId(R.id.toolbar_support)).check(matches(isDisplayed()));

        onView(withId(android.R.id.empty)).check(matches(isDisplayed()));
        onView(withId(android.R.id.list)).check(matches(not(isDisplayed())));
    }

    @Test
    public void oneWorkoutAdded_shouldBeDisplayed() throws Exception {
        onView(withId(R.id.fab_add_workout)).perform(click());

        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)));
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
    }

    @After
    public void tearDown() throws Exception {
        activityRule.deleteWorkouts();
    }
}