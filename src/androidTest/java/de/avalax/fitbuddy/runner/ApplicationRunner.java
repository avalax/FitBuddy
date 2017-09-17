package de.avalax.fitbuddy.runner;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.NumberPicker;

import org.hamcrest.Matcher;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.dialog.WeightDecimalPlaces;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsChecked;
import static de.avalax.fitbuddy.runner.BottomNavigationMatcher.bottomNavItemIsNotChecked;
import static java.lang.Integer.parseInt;
import static org.hamcrest.Matchers.not;

public class ApplicationRunner {
    public void showsStartBottomNavAsActive() {
        onView(withId(R.id.navigation_start_item)).check(matches(bottomNavItemIsChecked()));
        onView(withId(R.id.navigation_workout_item)).check(matches(bottomNavItemIsNotChecked()));
        onView(withId(R.id.navigation_statistics_item)).check(matches(bottomNavItemIsNotChecked()));
    }

    public void hasShownAddNewWorkoutHint() {
        onView(withId(android.R.id.empty)).check(matches(withText(R.string.empty_workout_list)));
        onView(withId(android.R.id.list)).check(matches(not(isDisplayed())));
    }

    public void hasShownAddNewExerciseHint() {
        onView(withId(android.R.id.empty)).check(matches(withText(R.string.empty_exercise_list)));
        onView(withId(android.R.id.list)).check(matches(not(isDisplayed())));
    }

    public void hasShownAddNewSetHint() {
        onView(withId(android.R.id.empty)).check(matches(withText(R.string.empty_set_list)));
        onView(withId(android.R.id.list)).check(matches(not(isDisplayed())));
    }

    public void addWorkout(String name) {
        onView(withId(R.id.fab_add_workout)).perform(click());
        onView(withId(R.id.edit_text_workout_name)).perform(typeText(name), closeSoftKeyboard());
    }

    public void addExercise(String name) {
        onView(withId(R.id.fab_add_exercise)).perform(click());
        onView(withId(R.id.edit_text_exercise_name)).perform(typeText(name), closeSoftKeyboard());
    }

    public void addSet(String reps, String weight) {
        onView(withId(R.id.fab_add_set)).perform(click());

        onView(withId(R.id.set_reps)).perform(click());
        onView(withId(R.id.repsNumberPicker)).perform(setNumberPicker(parseInt(reps)));
        onView(withId(R.id.done_button)).perform(click());

        String[] weightSplit = weight.split("\\.");
        onView(withId(R.id.set_weight)).perform(click());
        onView(withId(R.id.numberPicker)).perform(setNumberPicker(parseInt(weightSplit[0])));
        WeightDecimalPlaces weightDecimalPlaces = new WeightDecimalPlaces();
        int position = weightDecimalPlaces.getPosition(Double.parseDouble("0." + weightSplit[1]));
        onView(withId(R.id.decimalPlaces)).perform(setNumberPicker(position));
        onView(withId(R.id.done_button)).perform(click());
    }

    public void saveSet() {
        onView(withId(R.id.toolbar_save_set)).perform(click());
    }

    public void hasShownSetAddedToExercise(String reps, String weight) {
        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)));
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.item_title)).check(matches(withText(reps)));
        onView(withId(R.id.item_subtitle)).check(matches(withText(weight)));
    }

    public void saveExercise() {
        onView(withId(R.id.toolbar_save_exercise)).perform(click());
    }

    public void hasShownExerciseAddedToWorkout(String name, String details) {
        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)));
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.item_title)).check(matches(withText(name)));
        onView(withId(R.id.item_subtitle)).check(matches(withText(details)));
    }

    public void saveWorkout() {
        onView(withId(R.id.toolbar_save_workout)).perform(click());
    }

    public void hasShownWorkoutAdded() {
        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)));
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.card_title)).check(matches(withText("new workout")));
        onView(withId(R.id.card_subtitle)).check(matches(withText("Executed 0 times")));
        onView(withId(R.id.card_date)).check(matches(withText("never")));
        onView(withId(R.id.btn_select_workout)).check(matches(withText("START")));
        onView(withId(R.id.btn_share_workout)).check(matches(isDisplayed()));
    }

    public void hasShownSetDetails(String reps, String weight) {
        onView(withId(R.id.set_reps_text_view)).check(matches(withText(reps)));
        onView(withId(R.id.set_weight_text_view)).check(matches(withText(weight)));
    }

    private static ViewAction setNumberPicker(final int value) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((NumberPicker) view).setValue(value);
            }

            @Override
            public String getDescription() {
                return "Set the value into a NumberPicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }
}
