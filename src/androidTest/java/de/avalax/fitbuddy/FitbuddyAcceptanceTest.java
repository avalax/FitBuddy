package de.avalax.fitbuddy;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.runner.ApplicationRunner;
import de.avalax.fitbuddy.runner.FitbuddyActivityTestRule;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FitbuddyAcceptanceTest {

    /**
     * private int actionBarTitleId() {
     * Resources resources = activityRule.getActivity().getResources();
     * return resources.getIdentifier("action_bar_title", "id", "android");
     * }
     **/

    private ApplicationRunner application = new ApplicationRunner();

    @Rule
    public FitbuddyActivityTestRule activityRule = new FitbuddyActivityTestRule(
            MainActivity.class);

    @Test
    public void initialStart_shouldShowEmptyStartFragment() throws Exception {
        application.showsStartBottomNavAsActive();
        application.hasShownAddNewWorkoutHint();
    }

    @Test
    public void oneWorkoutAdded_shouldBeDisplayed() throws Exception {
        application.addWorkout("new workout");
        application.hasShownAddNewExerciseHint();

        application.addExercise("new exercise");
        application.hasShownAddNewSetHint();

        application.addSet("12", "42");
        //TODO: use and verify number picker / weight chooser

        application.saveSet();
        //TODO: 42 instead of 42.0
        application.hasShownSetAddedToExercise("12", "42.0");

        application.saveExercise();
        //TODO: hardcoded 1 x 12
        application.hasShownExerciseAddedToWorkout("new exercise", "1 x 12");

        application.saveWorkout();
        application.hasShownWorkoutAdded();
    }

    //TODO:
    //         onView(withId(R.id.toolbar_support)).check(matches(isDisplayed()));

    @After
    public void tearDown() throws Exception {
        activityRule.deleteWorkouts();
    }
}
