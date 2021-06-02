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

import java.util.List;

import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.DeleteViewAction;
import fr.plopez.mareu.utils.FakeMeetingsGen;
import fr.plopez.mareu.utils.RecyclerViewItemCountAssertion;
import fr.plopez.mareu.view.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
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

    private static final List<Meeting> meetingList = FakeMeetingsGen.generateFakeMeetingList(RoomsRepository.getRoomsRepositoryInstance());
    private static final int ITEMS_COUNT = meetingList.size();
    private static final int SELECTED_MEETING_POSITION = 1;
    private static final String FILTER_DIALOG_MATCH_TEXT = "1 " + ApplicationProvider.getApplicationContext().getString(R.string.filter_text_message_singular);

    @Rule
    public final ActivityScenario<MainActivity> mActivityRule = new ActivityScenarioRule<>(MainActivity.class).getScenario();

    @Before
    public void setUp() {
        assertThat(mActivityRule, notNullValue());
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

        // verify that the add activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_test() {
        // click on the filter action button to load the filter fragment
        onView(withId(R.id.sort_meetings)).perform(click());

        // verify that the add activity is correctly loaded
        onView(withId(R.id.filter_fragment_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_and_back_to_main_activity_test() {
        // click on the filter action button to load the filter fragment
        onView(withId(R.id.sort_meetings)).perform(click());

        // click on close button
        onView(withId(R.id.closeFilterDialogButton)).perform(click());

        // verify that the add activity is correctly loaded
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
                        2, click())
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
        // verify we have the right number of meetings
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(ITEMS_COUNT));

        // click on the second meeting holder delete button
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        SELECTED_MEETING_POSITION, new DeleteViewAction()));

        // verify that the holder is deleted
        onView(allOf(withId(R.id.main_activity_fragment_meeting_recycler_view), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(ITEMS_COUNT-1));
    }
}