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
    public void newWorkout_shouldBeDisplayed() throws Exception {
        application.addWorkout("new workout");
        application.hasShownAddNewExerciseHint();

        application.addExercise("new exercise");
        application.hasShownAddNewSetHint();

        application.addSet("15", "42.5");
        application.hasShownSetDetails("15", "42.5");

        application.saveSet();
        application.hasShownSetAddedToExercise("15", "42.5");

        application.saveExercise();
        application.hasShownExerciseDetails("new exercise", "1 x 15");

        application.saveWorkout();
        application.hasShownWorkoutDetails(0, "new workout");
    }

    @Test
    public void existingWorkout_shouldDisplayChanges() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("old workout");
        application.addExercise("old exercise");
        application.addSet("15", "42.5");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.selectWorkout(0);
        application.hasShownOldWorkoutNameInEditView("old workout");
        application.changeWorkout("new workout");

        application.selectExercise(0);
        application.hasShownOldExerciseNameInEditView("old exercise");
        application.changeExercise("new exercise");

        application.selectSet(0);
        application.hasShownOldSetDetails("15", "42.5");
        application.changeSet("12", "47.5");

        application.saveSet();
        application.hasShownSetAddedToExercise("12", "47.5");

        application.saveExercise();
        application.hasShownExerciseDetails("new exercise", "1 x 12");
        application.saveWorkout();
        application.hasShownWorkoutDetails(0, "new workout");
    }

    @After
    public void tearDown() throws Exception {
        activityRule.deleteWorkouts();
    }
}
