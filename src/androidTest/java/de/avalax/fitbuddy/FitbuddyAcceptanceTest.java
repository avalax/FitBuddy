package de.avalax.fitbuddy;

import android.content.res.Resources;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.runner.FitbuddyActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsChecked;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsNotChecked;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FitbuddyAcceptanceTest {

    private int actionBarTitleId() {
        Resources resources = activityRule.getActivity().getResources();
        return resources.getIdentifier("action_bar_title", "id", "android");
    }

    @Rule
    public FitbuddyActivityTestRule activityRule = new FitbuddyActivityTestRule(
            MainActivity.class);

    @Test
    public void initialStart_shouldShowStartFragmentOnly() throws Exception {
        activityRule.launchActivity();

        onView(withId(R.id.navigation_start_item)).check(matches(bottomNavItemIsChecked()));
        onView(withId(R.id.navigation_workout_item)).check(matches(bottomNavItemIsNotChecked()));
        onView(withId(R.id.navigation_statistics_item)).check(matches(bottomNavItemIsNotChecked()));
    }

    @Test
    @Ignore
    public void switchedToWorkout_shouldShowWorkoutActivity() throws Exception {
        BasicWorkout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.setName("first_exercise");
        exercise.getSets().createSet();
        activityRule.getWorkoutSession().switchWorkout(workout);

        activityRule.launchActivity();

        onView(withId(R.id.navigation_start_item)).check(matches(isEnabled()));
        onView(withId(R.id.navigation_start_item)).perform(click());

        onView(withId(actionBarTitleId())).check(matches(withText("first_exercise")));
    }
}
