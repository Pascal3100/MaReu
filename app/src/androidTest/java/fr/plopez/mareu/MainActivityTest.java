package fr.plopez.mareu;

import androidx.test.core.app.ActivityScenario;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule mActivityRule = new ActivityScenarioRule(MainActivity.class);

    private static final List<Meeting> meetingList = FakeMeetingsGen.generateFakeMeetingList(RoomsRepository.getRoomsRepositoryInstance());
    private static final int ITEMS_COUNT = meetingList.size();
    private static final int SELECTED_MEETING_POSITION = 1;


    @Before
    public void setUp() {
        ActivityScenario activity = mActivityRule.getScenario();
        assertThat(activity, notNullValue());
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
        // Don't works...
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        // verify that the add activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_test() {
        // click on the filter action button to load the filter fragment
        // don't find any solution...

        // verify that the add activity is correctly loaded
        onView(withId(R.id.filter_fragment_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void jump_to_filter_fragment_and_back_to_main_activity_test() {
        // click on the filter action button to load the filter fragment
        // don't find any solution...

        // click on close button
        onView(withId(R.id.closeFilterDialogButton)).perform(click());

        // verify that the add activity is correctly loaded
        onView(withId(R.id.main_activity_container))
                .check(matches(isDisplayed()));
    }

    @Test
    public void main_activity_delete_meeting_test() {
        // verify we have the right number of meetings
        onView(allOf(withId(R.id.meeting_container), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(ITEMS_COUNT));

        // click on the second meeting holder delete button
        onView(allOf(withId(R.id.meeting_container), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        SELECTED_MEETING_POSITION, new DeleteViewAction()));

        // verify that the holder is deleted
        onView(allOf(withId(R.id.meeting_container), isDisplayed()))
                .check(RecyclerViewItemCountAssertion.withItemCount(ITEMS_COUNT-1));
    }
}