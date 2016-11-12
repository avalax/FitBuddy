package de.avalax.fitbuddy;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyModule;
import de.avalax.fitbuddy.presentation.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private int actionBarTitleId() {
        Resources resources = mActivityRule.getActivity().getResources();
        return resources.getIdentifier("action_bar_title", "id", "android");
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Inject
    WorkoutSession workoutSession;

    @Before
    public void setUp() throws Exception {
        Context context = mActivityRule.getActivity().getApplicationContext();
        TestComponent component = DaggerTestComponent.builder()
                .fitbuddyModule(new FitbuddyModule(context)).build();
        component.inject(this);
    }

    @Test
    public void startWorkout_shouldShowWorkoutActivity() throws Exception {
        BasicWorkout workout = new BasicWorkout();
        Exercise exercise = workout.getExercises().createExercise();
        exercise.setName("first_exercise");
        exercise.getSets().createSet();
        workoutSession.switchWorkout(workout);

        onView(withId(R.id.continue_workout_button)).perform(click());

        onView(withId(actionBarTitleId())).check(matches(withText("first_exercise")));
    }
}
