package de.avalax.fitbuddy.runner;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.dialog.WeightDecimalPlaces;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.*;
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

    public void selectWorkout(int position) {
        onView(withId(android.R.id.list)).perform(
                actionOnItemAtPosition(position, clickChildButtonWithId(R.id.btn_edit_workout)));
    }

    public void changeWorkout(String name) {
        onView(withId(R.id.edit_text_workout_name)).perform(replaceText(name), closeSoftKeyboard());
    }


    public void hasShownOldWorkoutNameInEditView(String name) {
        onView(withId(R.id.edit_text_workout_name)).check(matches(withText(name)));
    }

    public void addExercise(String name) {
        onView(withId(R.id.fab_add_exercise)).perform(click());
        onView(withId(R.id.edit_text_exercise_name)).perform(typeText(name), closeSoftKeyboard());
    }

    public void selectExercise(int position) {
        onView(withId(android.R.id.list)).perform(
                actionOnItemAtPosition(position, click()));
    }

    public void hasShownOldExerciseNameInEditView(String name) {
        onView(withId(R.id.edit_text_exercise_name)).check(matches(withText(name)));
    }

    public void changeExercise(String name) {
        onView(withId(R.id.edit_text_exercise_name)).perform(replaceText(name), closeSoftKeyboard());
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

    public void hasShownExerciseDetails(String name, String details) {
        onView(withId(android.R.id.list)).check(matches(hasChildCount(1)));
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.item_title)).check(matches(withText(name)));
        onView(withId(R.id.item_subtitle)).check(matches(withText(details)));
    }

    public void saveWorkout() {
        onView(withId(R.id.toolbar_save_workout)).perform(click());
    }

    public void hasShownWorkoutDetails(int position, String name) {
        onView(withId(android.R.id.empty)).check(matches(not(isDisplayed())));
        onView(withId(android.R.id.list))
                .perform(RecyclerViewActions.scrollToPosition(position))
                .check(matches(itemAtPosition(position, withText(name),R.id.card_title)))
                .check(matches(itemAtPosition(position, withText("Executed 0 times"),R.id.card_subtitle)))
                .check(matches(itemAtPosition(position, withText("never"),R.id.card_date)))
                .check(matches(itemAtPosition(position, withText(R.string.btn_start_workout),R.id.btn_start_workout)))
                .check(matches(itemAtPosition(position, withText(R.string.btn_edit_workout),R.id.btn_edit_workout)))
                .check(matches(itemAtPosition(position, isDisplayed(),R.id.btn_share_workout)));
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

    public static ViewAction clickChildButtonWithId(final int id) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(Button.class);
            }
        };
    }

    public static Matcher<View> itemAtPosition(int position, Matcher<View> itemMatcher, int id) {

        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has view id " + itemMatcher + " at position " + position);
            }

            @Override
            public boolean matchesSafely(final RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                View targetView = viewHolder.itemView.findViewById(id);
                return itemMatcher.matches(targetView);
            }
        };
    }
}
