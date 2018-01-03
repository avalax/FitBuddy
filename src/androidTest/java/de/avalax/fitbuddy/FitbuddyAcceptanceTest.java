package de.avalax.fitbuddy;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder;
import de.avalax.fitbuddy.domain.model.set.BasicSetBuilder;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.runner.ApplicationRunner;
import de.avalax.fitbuddy.runner.FitbuddyActivityTestRule;
import de.avalax.fitbuddy.runner.PersistenceRunner;

import static de.avalax.fitbuddy.domain.model.exercise.BasicExerciseBuilder.anExercise;
import static de.avalax.fitbuddy.domain.model.set.BasicSetBuilder.aSet;
import static de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder.aWorkout;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FitbuddyAcceptanceTest {

    private ApplicationRunner application;
    private PersistenceRunner persistence;

    @Rule
    public FitbuddyActivityTestRule activityRule = new FitbuddyActivityTestRule(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        application = new ApplicationRunner();
        persistence = new PersistenceRunner(activityRule);
    }

    @Test
    public void initialStart_shouldShowEmptyStartFragment() throws Exception {
        activityRule.launchActivity(null);

        application.showsStartBottomNavAsActive();
        application.hasShownAddNewWorkoutHint();
        application.showsSupportMenuItem();
        application.showsWorkoutBottomNavAsDisabled();
        application.showsFinishedWorkoutBottomNavAsDisabled();
        application.showsAdMob();
    }

    @Test
    public void newWorkout_shouldBeDisplayed() throws Exception {
        activityRule.launchActivity(null);

        application.addWorkout("new workout");
        application.hasShownAddNewExerciseHint();

        application.addExercise("new exercise");
        application.hasShownAddNewSetHint();

        application.addSet("15", "42.5");
        application.hasShownSetDetails("15 reps", "42.5 kg");

        application.saveSet();
        application.hasShownSetAddedToExercise("15 reps", "42.5 kg");

        application.saveExercise();
        application.hasShownExerciseDetails("new exercise", "1 x 15", "42.5 kg");

        application.saveWorkout();
        application.hasShownWorkoutDetails(0, "new workout", "Never", "Executed 0 times");
    }

    @Test
    public void existingWorkout_shouldDisplayChanges() throws Exception {
        BasicSetBuilder set = aSet().withWeight(42.5).withMaxReps(15);
        BasicExerciseBuilder exercise = anExercise().withName("old exercise").withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withName("old workout").withExercise(exercise);
        Workout persistedWorkout = persistence.addWorkout(workout);
        persistence.finishWorkout(persistedWorkout);

        activityRule.launchActivity(null);

        application.editWorkout(0);
        application.hasShownOldWorkoutNameInEditView("old workout");
        application.changeWorkout("new workout");

        application.editExercise(0);
        application.hasShownOldExerciseNameInEditView("old exercise");
        application.changeExercise("new exercise");

        application.editSet(0);
        application.hasShownOldSetDetails("15 reps", "42.5 kg");
        application.changeSet("12", "47.5");

        application.saveSet();
        application.hasShownSetAddedToExercise("12 reps", "47.5 kg");

        application.saveExercise();
        application.hasShownExerciseDetails("new exercise", "1 x 12", "47.5 kg");
        application.saveWorkout();
        application.hasShownWorkoutDetails(0, "new workout", "Today", "Executed 1 time");
    }

    @Test
    public void existingWorkout_shouldNotSaveExerciseWithoutSets() throws Exception {
        BasicExerciseBuilder exercise = anExercise().withSet(aSet()).withSet(aSet());
        BasicWorkoutBuilder workout = aWorkout().withExercise(exercise);
        persistence.addWorkout(workout);

        activityRule.launchActivity(null);

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
    public void existingWorkout_shouldNotSaveWorkoutOnBack() throws Exception {
        BasicSetBuilder set = aSet().withMaxReps(12);
        BasicExerciseBuilder exercise = anExercise().withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withName("old workout name").withExercise(exercise);
        persistence.addWorkout(workout);
        activityRule.launchActivity(null);
        application.editWorkout(0);

        application.changeWorkout("new workout name");
        application.cancelAction();

        application.hasShownWorkoutDetails(0, "old workout name", "Never", "Executed 0 times");
    }

    @Test
    public void existingExercise_shouldNotSaveExerciseOnBack() throws Exception {
        BasicSetBuilder set = aSet().withMaxReps(12).withWeight(0);
        BasicExerciseBuilder exercise = anExercise().withName("old exercise name").withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withExercise(exercise);
        persistence.addWorkout(workout);
        activityRule.launchActivity(null);
        application.editWorkout(0);

        application.editExercise(0);
        application.changeExercise("new exercise name");
        application.cancelAction();

        application.hasShownExerciseDetails("old exercise name", "1 x 12", "no weight");
    }

    @Test
    public void existingSet_shouldNotSaveExerciseOnBack() throws Exception {
        BasicSetBuilder set = aSet().withMaxReps(12).withWeight(0);
        BasicExerciseBuilder exercise = anExercise().withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withExercise(exercise);
        persistence.addWorkout(workout);
        activityRule.launchActivity(null);
        application.editWorkout(0);
        application.editExercise(0);

        application.editSet(0);
        application.changeSet("15", "100.0");
        application.cancelAction();

        application.hasShownSetAddedToExercise("12 reps", "no weight");
    }

    @Test
    public void existingWorkout_shouldNotSaveWorkoutWithoutExercises() throws Exception {
        BasicWorkoutBuilder workout = aWorkout().withExercise(anExercise()).withExercise(anExercise());
        persistence.addWorkout(workout);

        activityRule.launchActivity(null);

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
    public void activeWorkoutGiven_shouldNavigateBetweenFragments() throws Exception {
        BasicSetBuilder set = aSet().withWeight(42).withMaxReps(12);
        BasicExerciseBuilder exercise = anExercise().withName("an exercise").withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withName("a workout").withExercise(exercise);
        Workout persistedWorkout = persistence.addWorkout(workout);
        persistence.finishWorkout(persistedWorkout);

        activityRule.launchActivity(null);

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
        application.showsFinishedWorkoutOverview(0, "a workout", "Today", "normal");

        application.selectFinishedWorkout(0);
        application.showsFinishedWorkoutDetail("a workout", "Today", "Executed 1 time");

        application.backPressed();
        application.showsFinishedWorkoutOverview(0, "a workout", "Today", "normal");

        application.backPressed();
        application.showsWorkoutIsActive(0);
    }

    @Test
    public void doWorkout_shouldDisplaySwipeEvents() throws Exception {
        BasicSetBuilder set1 = aSet().withWeight(42).withMaxReps(12);
        BasicSetBuilder set2 = aSet().withWeight(55).withMaxReps(12);
        BasicExerciseBuilder exercise1 = anExercise().withName("first exercise").withSet(set1).withSet(set2);
        BasicSetBuilder set3 = aSet().withWeight(10).withMaxReps(12);
        BasicExerciseBuilder exercise2 = anExercise().withName("second exercise").withSet(set3);
        BasicWorkoutBuilder workout = aWorkout().withName("a workout").withExercise(exercise1).withExercise(exercise2);
        persistence.addWorkout(workout);

        activityRule.launchActivity(null);

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
        BasicSetBuilder set = aSet().withWeight(42).withMaxReps(12);
        BasicExerciseBuilder exercise = anExercise().withName("only one exercise").withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withName("a workout").withExercise(exercise);
        persistence.addWorkout(workout);

        activityRule.launchActivity(null);

        application.selectWorkout(0);
        application.showsNoPreviousExercise();
        application.showsActiveExercise("only one exercise", "42.0 kg");
        application.showsNoNextExercise();

        application.switchToPreviousExercise();
        application.showsActiveExercise("only one exercise", "42.0 kg");

        application.switchToNextExercise();
        application.hasShownFinishExerciseHint();
        application.finishWorkout();
        application.showsFinishedWorkoutOverview(0, "a workout", "Today", "normal");
    }

    @Test
    public void aWorkout_shouldBeDeleted() throws Exception {
        persistence.addWorkout(aWorkout());

        activityRule.launchActivity(null);

        application.deleteWorkout(0);

        application.hasShownAddNewWorkoutHint();
        application.showsSupportMenuItem();
    }

    @Test
    public void aFinishedWorkout_shouldBeDeleted() throws Exception {
        Workout persistedWorkout = persistence.addWorkout(aWorkout());
        persistence.finishWorkout(persistedWorkout);

        activityRule.launchActivity(null);

        application.switchToSummary();
        application.deleteFinishedWorkout(0);

        application.hasShownAddNoFinishedWorkoutsHint();
    }

    @Test
    public void aFinishedWorkout_shouldShowExerciseDetails() throws Exception {
        BasicSetBuilder set = aSet().withWeight(42).withMaxReps(12);
        BasicExerciseBuilder exercise = anExercise().withName("an exercise").withSet(set);
        BasicWorkoutBuilder workout = aWorkout().withName("a workout").withExercise(exercise);
        Workout persistedWorkout = persistence.addWorkout(workout);
        persistence.finishWorkout(persistedWorkout);

        activityRule.launchActivity(null);

        application.switchToSummary();
        application.selectFinishedWorkout(0);

        application.showsFinishedExerciseDetail(0, "an exercise", "1 x 12");
    }

    @After
    public void tearDown() throws Exception {
        persistence.deleteWorkouts();
        persistence.deleteFinishedWorkouts();
    }
}
