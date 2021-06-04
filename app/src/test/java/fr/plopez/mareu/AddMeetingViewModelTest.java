package fr.plopez.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.utils.LiveDataTestUtils;
import fr.plopez.mareu.view.add.AddMeetingViewAction;
import fr.plopez.mareu.view.add.AddMeetingViewModel;
import fr.plopez.mareu.view.model.MeetingRoomItem;

import static fr.plopez.mareu.view.add.AddMeetingViewAction.DISPLAY_EMPTY_EMAIL_MESSAGE;
import static fr.plopez.mareu.view.add.AddMeetingViewAction.DISPLAY_INCORRECT_EMAIL_MESSAGE;
import static fr.plopez.mareu.view.add.AddMeetingViewAction.DISPLAY_INCORRECT_ROOM_MESSAGE;
import static fr.plopez.mareu.view.add.AddMeetingViewAction.DISPLAY_INCORRECT_SUBJECT_MESSAGE;
import static fr.plopez.mareu.view.add.AddMeetingViewAction.FINISH_ACTIVITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class AddMeetingViewModelTest {

    // constants
    private final String CORRECT_STRING_INPUT = "CORRECT_STRING_INPUT";
    private final String EMPTY_STRING_INPUT = "";
    private final String CORRECT_ROOM_INPUT = "Mushroom";
    private final List<String> FAKE_MEETING_EMAIL_LIST = Arrays.asList("pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com");

    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // mocks
    @Mock
    public MeetingsRepository meetingsRepository;
    @Mock
    public RoomFilterRepository roomFilterRepository;

    // class variables
    private AddMeetingViewModel addMeetingViewModel;


    @Before
    public void setUp() {

        Mockito.doReturn(new Room("Mushroom", R.drawable.ic_room_mushroom))
                .when(roomFilterRepository)
                .getRoomByName("Mushroom");

        Mockito.doReturn(Arrays.asList("Flower", "Leaf", "Mushroom"))
                .when(roomFilterRepository)
                .getRoomsNames();

        MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListLiveData = new MutableLiveData<>();
        List<MeetingRoomItem> meetingRoomItemList = new ArrayList<>();
        meetingRoomItemList.add(new MeetingRoomItem("Mushroom", R.drawable.ic_room_mushroom));
        meetingRoomItemList.add(new MeetingRoomItem("Flower", R.drawable.ic_room_flower));
        meetingRoomItemList.add(new MeetingRoomItem("Leaf", R.drawable.ic_room_leaf));
        meetingRoomItemListLiveData.setValue(meetingRoomItemList);
        Mockito.doReturn(meetingRoomItemListLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();

        addMeetingViewModel = new AddMeetingViewModel(
                meetingsRepository,
                roomFilterRepository);

        addMeetingViewModel.updateTextFilterPattern("");
    }

    // ----------------------------------------------------------------------------
    //            SUBJECT SECTION TESTS
    // ----------------------------------------------------------------------------

    // Test that subject is correctly set
    @Test
    public void correct_subject_case_test() {

        // Given
        addMeetingViewModel.addSubject(CORRECT_STRING_INPUT);

        // When
        AddMeetingViewAction result = addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent().getValue();

        // Then
        assertNotEquals(DISPLAY_INCORRECT_SUBJECT_MESSAGE, result);
    }

    // Test that an error message is thrown when the subject is empty
    @Test
    public void empty_subject_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addSubject(EMPTY_STRING_INPUT);

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_SUBJECT_MESSAGE, result);
    }

    // ----------------------------------------------------------------------------
    //            ROOM SECTION TESTS
    // ----------------------------------------------------------------------------

    // Test that room is correctly set
    @Test
    public void correct_room_case_test() {

        // Given
        addMeetingViewModel.addRoom(CORRECT_ROOM_INPUT);

        // When
        AddMeetingViewAction result = addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent().getValue();

        // Then
        assertNotEquals(DISPLAY_INCORRECT_ROOM_MESSAGE, result);
    }

    // Test that an error message is thrown when the room is incorrect
    @Test
    public void incorrect_room_case_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addRoom(CORRECT_STRING_INPUT);

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_ROOM_MESSAGE, result);
    }

    // Test that an error message is thrown when the room is empty
    @Test
    public void empty_room_case_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addRoom(EMPTY_STRING_INPUT);

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_ROOM_MESSAGE, result);
    }

    // ----------------------------------------------------------------------------
    //            EMAIL SECTION TESTS
    // ----------------------------------------------------------------------------

    // Test that an email is correctly added to list
    @Test
    public void nominal_case_add_email_test() {

        // Given
        String CORRECT_EMAIL_STRING_INPUT = "pascal.lopez@expleogroup.com";
        addMeetingViewModel.addEmail(CORRECT_EMAIL_STRING_INPUT);

        // When
        AddMeetingViewAction result = addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent().getValue();

        // Then
        assertNotEquals(DISPLAY_INCORRECT_EMAIL_MESSAGE, result);
    }

    // Test that an error message is thrown when trying to add empty email
    @Test
    public void empty_email_case_add_email_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addEmail(EMPTY_STRING_INPUT);

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_EMPTY_EMAIL_MESSAGE, result);
    }

    // Test that an error message is thrown when trying to add incorrect email
    @Test
    public void incorrect_email_case_add_email_test() throws InterruptedException {

        // Given
        String INCORRECT_EMAIL_STRING_INPUT = "INCORRECT_EMAIL_STRING_INPUT";
        addMeetingViewModel.addEmail(INCORRECT_EMAIL_STRING_INPUT);

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_EMAIL_MESSAGE, result);
    }

    // Test that an email is correctly removed from list
    @Test
    public void nominal_case_add_email_count_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(0));

        // When
        List<String> result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getEmailsListLiveData());

        // Then
        assertEquals(1, result.size());
    }

    // Test that an email is correctly removed from list
    @Test
    public void nominal_case_delete_email_count_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(0));
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(1));
        addMeetingViewModel.removeEmail(FAKE_MEETING_EMAIL_LIST.get(0));

        // When
        List<String> result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getEmailsListLiveData());

        // Then
        assertEquals(1, result.size());
    }

    // ----------------------------------------------------------------------------
    //            ADD MEETING SECTION TESTS
    // ----------------------------------------------------------------------------

    // Test that a new meeting is added to the repo
    @Test
    public void nominal_case_add_meeting_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addSubject(CORRECT_STRING_INPUT);
        addMeetingViewModel.addStartTime(CORRECT_STRING_INPUT);
        addMeetingViewModel.addRoom(CORRECT_ROOM_INPUT);
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(0));
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(1));
        addMeetingViewModel.addMeeting();

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(FINISH_ACTIVITY, result);
    }

    // Test that an error message is thrown when the subject is empty
    @Test
    public void empty_subject_case_add_meeting_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addSubject(EMPTY_STRING_INPUT);
        addMeetingViewModel.addStartTime(CORRECT_STRING_INPUT);
        addMeetingViewModel.addRoom(CORRECT_ROOM_INPUT);
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(0));
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(1));
        addMeetingViewModel.addMeeting();

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_SUBJECT_MESSAGE, result);
    }

    // Test that an error message is thrown when the room is empty
    @Test
    public void empty_room_case_add_meeting_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addSubject(CORRECT_STRING_INPUT);
        addMeetingViewModel.addStartTime(CORRECT_STRING_INPUT);
        addMeetingViewModel.addRoom(EMPTY_STRING_INPUT);
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(0));
        addMeetingViewModel.addEmail(FAKE_MEETING_EMAIL_LIST.get(1));
        addMeetingViewModel.addMeeting();

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_ROOM_MESSAGE, result);
    }

    // Test that an error message is thrown when the email list is empty
    @Test
    public void empty_email_case_add_meeting_test() throws InterruptedException {

        // Given
        addMeetingViewModel.addSubject(CORRECT_STRING_INPUT);
        addMeetingViewModel.addStartTime(CORRECT_STRING_INPUT);
        addMeetingViewModel.addRoom(CORRECT_ROOM_INPUT);
        addMeetingViewModel.addEmail(EMPTY_STRING_INPUT);
        addMeetingViewModel.addMeeting();

        // When
        AddMeetingViewAction result = LiveDataTestUtils
                .getOrAwaitValue(addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent());

        // Then
        assertEquals(DISPLAY_INCORRECT_EMAIL_MESSAGE, result);
    }
}
