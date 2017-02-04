package de.avalax.fitbuddy;

import android.content.res.Resources;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

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
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private int actionBarTitleId() {
        Resources resources = activityRule.getActivity().getResources();
        return resources.getIdentifier("action_bar_title", "id", "android");
    }

    @Rule
    public FitbuddyActivityTestRule activityRule = new FitbuddyActivityTestRule(
            MainActivity.class);

    @Test
    public void switchedToWorkout_shouldShowWorkoutActivity() throws Exception {
        BasicWorkout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.setName("first_exercise");
        exercise.getSets().createSet();
        activityRule.getWorkoutSession().switchWorkout(workout);

        activityRule.launchActivity();

        onView(withId(R.id.continue_workout_button)).check(matches(isEnabled()));
        onView(withId(R.id.continue_workout_button)).perform(click());

        onView(withId(actionBarTitleId())).check(matches(withText("first_exercise")));
    }

    @Test
    public void noWorkoutCreated_shouldShowMainActivity() throws Exception {
        activityRule.getWorkoutSession().switchWorkout(null);

        activityRule.launchActivity();

        onView(withId(R.id.continue_workout_button)).check(matches(not(isEnabled())));
    }
}
