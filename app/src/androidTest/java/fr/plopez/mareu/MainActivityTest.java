package fr.plopez.mareu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.plopez.mareu.utils.DeleteViewAction;
import fr.plopez.mareu.utils.RecyclerViewItemCountAssertion;
import fr.plopez.mareu.view.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private static final int SELECTED_MEETING_POSITION = 1;
    private static final String FILTER_DIALOG_MATCH_TEXT = "1 " + ApplicationProvider.getApplicationContext().getString(R.string.filter_text_message_singular);

    @Before
    public void setUp() {
        ActivityScenario<MainActivity> activityScenario = activityScenarioRule.getScenario();
        assertThat(activityScenario, notNullValue());

        // add two new meetings
        // ---------- First -----------
        // go to add view and fill fields
        onView(withId(R.id.main_activity_fragment_fab))
                .perform(click());
        onView(withId(R.id.text_input_subject_content)).perform(replaceText("new subject"), pressImeActionButton());
        onView(withId(R.id.room_selector_menu)).perform(replaceText("Mushroom"), pressImeActionButton());
        onView(withId(R.id.email_input_content)).perform(replaceText("anonyme1@gmail.com"), pressImeActionButton());

        // click on save meeting button
        onView(withId(R.id.save_button)).perform(click());

        // verify that the main activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));

        // ---------- Second -----------
        // go to add view and fill fields
        onView(withId(R.id.main_activity_fragment_fab))
                .perform(click());
        onView(withId(R.id.text_input_subject_content)).perform(replaceText("Other subject"), pressImeActionButton());
        onView(withId(R.id.room_selector_menu)).perform(replaceText("Leaf"), pressImeActionButton());
        onView(withId(R.id.email_input_content)).perform(replaceText("anonyme1@gmail.com"), pressImeActionButton());

        // click on save meeting button
        onView(withId(R.id.save_button)).perform(click());

        // verify that the main activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));

        // verify we have the right number of meetings
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(2));
    }

    @Test
    public void jump_to_add_activity_test() {
        // click on the floating action button to load the add activity
        onView(withId(R.id.main_activity_fragment_fab))
                .perform(click());

        // verify that the add activity is correctly loaded
        onView(withId(R.id.add_meeting_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void back_to_main_activity_from_add_activity_test() {
        // click on the floating action button to load the add activity
        onView(withId(R.id.main_activity_fragment_fab))
                .perform(click());

        // click on back navigation icon
        onView(withId(R.id.add_meeting_activity_app_bar)).perform(pressBack());

        // verify that the main activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_test() {
        // click on the filter action button to load the filter fragment
        onView(withId(R.id.sort_meetings)).perform(click());

        // verify that the filter fragment is correctly loaded
        onView(withId(R.id.filter_fragment_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_and_back_to_main_activity_test() {
        // click on the filter action button to load the filter fragment
        onView(withId(R.id.sort_meetings)).perform(click());

        // click on close button
        onView(withId(R.id.closeFilterDialogButton)).perform(click());

        // verify that the main activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_and_do_filter_test() {
        // click on the filter action button to load the filter fragment
        onView(withId(R.id.sort_meetings)).perform(click());

        // click on the flower room item filter
        onView(allOf(withId(R.id.room_filter), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        3, click())
                );

        // verify the text is updated the right way
        onView(withId(R.id.filter_text)).check(matches(withText(containsString(FILTER_DIALOG_MATCH_TEXT))));

        // click on close button
        onView(withId(R.id.closeFilterDialogButton)).perform(click());

        // verify that the list contain only the filtered meeting
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(1));
    }

    @Test
    public void main_activity_delete_meeting_test() {

        // click on the second meeting holder delete button
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        SELECTED_MEETING_POSITION, new DeleteViewAction()));

        // verify that the holder is deleted
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(1));
    }

    @Test
    public void main_activity_add_new_meeting_test(){
        // click on the floating action button to load the add activity
        onView(withId(R.id.main_activity_fragment_fab))
                .perform(click());
        onView(withId(R.id.text_input_subject_content)).perform(replaceText("newest subject"), pressImeActionButton());
        onView(withId(R.id.room_selector_menu)).perform(replaceText("Flower"), pressImeActionButton());
        onView(withId(R.id.email_input_content)).perform(replaceText("anonyme1@gmail.com"), pressImeActionButton());

        // click on save meeting button
        onView(withId(R.id.save_button)).perform(click());

        // verify that the main activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));

        // verify that the meeting is displayed
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(3));
    }
}