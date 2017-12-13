package de.avalax.fitbuddy;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setUp() throws Exception {
        activityRule.launchActivity(null);
    }

    @Test
    public void initialStart_shouldShowEmptyStartFragment() throws Exception {
        application.showsStartBottomNavAsActive();
        application.hasShownAddNewWorkoutHint();
        application.showsSupportMenuItem();
        application.showsWorkoutBottomNavAsDisabled();
        application.showsFinishedWorkoutBottomNavAsDisabled();
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

        application.editWorkout(0);
        application.hasShownOldWorkoutNameInEditView("old workout");
        application.changeWorkout("new workout");

        application.editExercise(0);
        application.hasShownOldExerciseNameInEditView("old exercise");
        application.changeExercise("new exercise");

        application.editSet(0);
        application.hasShownOldSetDetails("15", "42.5");
        application.changeSet("12", "47.5");

        application.saveSet();
        application.hasShownSetAddedToExercise("12", "47.5");

        application.saveExercise();
        application.hasShownExerciseDetails("new exercise", "1 x 12");
        application.saveWorkout();
        application.hasShownWorkoutDetails(0, "new workout");
    }

    @Test
    public void existingWorkout_shouldNotSaveExerciseWithoutSets() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("old workout");
        application.addExercise("old exercise");
        application.addSet("1", "42.5");
        application.saveSet();
        application.addSet("2", "42.5");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.editWorkout(0);
        application.editExercise(0);

        application.enterSetSelectionMode(0);
        application.selectSets(1);
        application.hasShownDeleteSetsCount(2);
        application.selectSets(0);
        application.hasShownDeleteSetsCount(1);
        application.selectSets(0);
        application.hasShownDeleteSetsCount(2);
        application.deleteSelectedSets();
        application.hasShownAddNewSetHint();
        application.saveExercise();
        application.hasShownCantSaveExerciseWithoutSets();
    }

    @Test
    public void existingWorkout_shouldNotSaveWorkoutWithoutExercises() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("old workout");
        application.addExercise("first exercise");
        application.addSet("1", "42.5");
        application.saveSet();
        application.saveExercise();
        application.addExercise("second exercise");
        application.addSet("1", "42.5");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.editWorkout(0);

        application.enterExerciseSelectionMode(0);
        application.selectExercises(1);
        application.hasShownDeleteExerciseCount(2);
        application.selectExercises(0);
        application.hasShownDeleteExerciseCount(1);
        application.selectExercises(0);
        application.hasShownDeleteExerciseCount(2);
        application.deleteSelectedExercices();
        application.hasShownAddNewExerciseHint();
        application.saveWorkout();
        application.hasShownCantSaveWorkoutWithoutExercices();
    }

    @Test
    public void existingWorkout_shouldBeDeleted() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("an exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.deleteWorkout(0);

        application.hasShownAddNewWorkoutHint();
        application.showsSupportMenuItem();
    }

    @Test
    public void activeWorkoutGiven_shouldNavigateBetweenFragments() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("an exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();
        //TODO: use provider for finished workout
        application.selectWorkout(0);
        application.switchToNextExercise();
        application.finishWorkout();
        application.navigateToStart();

        application.selectWorkout(0);
        application.showsActiveExercise("an exercise", "42.0 kg");

        application.navigateToStart();
        application.showsWorkoutIsActive(0);

        application.navigateToWorkout();
        application.showsActiveExercise("an exercise", "42.0 kg");

        application.backPressed();
        application.showsWorkoutIsActive(0);

        application.switchToSummary();
        application.showsFinishedWorkoutBottomNavAsActive();
        application.showsFinishedWorkoutOverview(0, "a workout");

        application.backPressed();
        application.showsWorkoutIsActive(0);
    }

    @Test
    public void doWorkout_shouldDisplaySwipeEvents() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("first exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.addSet("12", "55.0");
        application.saveSet();
        application.saveExercise();
        application.addExercise("second exercise");
        application.addSet("15", "10.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.selectWorkout(0);
        application.hasShownWorkoutProgress(0);
        application.hasShownActiveSets(1);
        application.hasShownRepsExecuted(0);

        application.addRepToExercise();
        application.hasShownRepsExecuted(1);
        application.hasShownWorkoutProgress(2);

        application.switchToNextSet();
        application.hasShownActiveSets(2);
        application.showsNoPreviousExercise();
        application.showsActiveExercise("first exercise", "55.0 kg");
        application.showsNextExercise("second exercise");

        application.switchToNextExercise();
        application.showsPreviousExercise("first exercise");
        application.showsActiveExercise("second exercise", "10.0 kg");
        application.showsNoNextExercise();
        application.hasShownActiveSets(1);
        application.hasShownRepsExecuted(0);

        application.switchToPreviousExercise();
        application.showsActiveExercise("first exercise", "55.0 kg");
    }

    @Test
    public void doWorkout_shouldFinishAfterLastExercise() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("only one exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();

        application.selectWorkout(0);
        application.showsNoPreviousExercise();
        application.showsActiveExercise("only one exercise", "42.0 kg");
        application.showsNoNextExercise();

        application.switchToPreviousExercise();
        application.showsActiveExercise("only one exercise", "42.0 kg");

        application.switchToNextExercise();
        application.hasShownFinishExerciseHint();
        application.finishWorkout();
        application.showsFinishedWorkout("a workout");
    }

    @Test
    public void aFinishedWorkout_shouldSeeDetailOverSummary() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("only one exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();
        application.selectWorkout(0);
        application.switchToNextExercise();
        application.finishWorkout();

        application.switchToSummary();
        application.selectFinishedWorkout(0);

        application.showsFinishedWorkout("a workout");
    }

    @Test
    public void aFinishedWorkout_shouldBeDeleted() throws Exception {
        //TODO: use provider for arrange
        application.addWorkout("a workout");
        application.addExercise("only one exercise");
        application.addSet("12", "42.0");
        application.saveSet();
        application.saveExercise();
        application.saveWorkout();
        application.selectWorkout(0);
        application.switchToNextExercise();
        application.finishWorkout();

        application.switchToSummary();
        application.deleteFinishedWorkout(0);

        application.hasShownAddNoFinishedWorkoutsHint();
    }

    @After
    public void tearDown() throws Exception {
        activityRule.deleteWorkouts();
        activityRule.deleteFinishedWorkouts();
    }
}
