package fr.plopez.mareu;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.TimeFilterRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.utils.FakeMeetingsGen;
import fr.plopez.mareu.utils.LiveDataTestUtils;
import fr.plopez.mareu.utils.TimeGen;
import fr.plopez.mareu.view.main.filter_fragment_dialog.FilterDialogFragmentViewModel;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;

@RunWith(MockitoJUnitRunner.class)
public class FilterDialogFragmentViewModelTest {

    // constants
    private final String MOCKED_SINGULAR_STRING = "MOCKED_SINGULAR_STRING";
    private final String MOCKED_PLURAL_STRING = "MOCKED_PLURAL_STRING";

    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // mocks
    @Mock
    public Application mockApplication;
    @Mock
    public MeetingsRepository meetingsRepository;
    @Mock
    public RoomsRepository roomsRepository;
    @Mock
    private RoomFilterRepository roomFilterRepository;
    @Mock
    private TimeFilterRepository timeFilterRepository;

    // class variables
    private FilterDialogFragmentViewModel filterDialogFragmentViewModel;

    private final MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();


    @Before
    public void setUp() {

        wireUpRoomRepository();

        wireUpRoomFilter();

        wireUpTimeFilter();

        wireUpMeetingRepository();

        when(mockApplication.getString(R.string.filter_text_message_singular))
                .thenReturn(MOCKED_SINGULAR_STRING);
        when(mockApplication.getString(R.string.filter_text_message_plural))
                .thenReturn(MOCKED_PLURAL_STRING);

        // Instantiate main Activity Dialog Fragment View Model
        filterDialogFragmentViewModel = new FilterDialogFragmentViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository,
                mockApplication);
    }

    // Test that list of meetings are correctly not filtered and counted
    @Test
    public void test_nominal_case_no_filter() throws InterruptedException {

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(filterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "3 " + MOCKED_PLURAL_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by room
    @Test
    public void test_nominal_case_room_filter() throws InterruptedException {

        // Given - setting mushroom room selected
        List<MeetingRoomItem> meetingRoomItemList = getDefaultMeetingRoomItems();
        meetingRoomItemList.get(2).setChecked(true);
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(filterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "1 " + MOCKED_SINGULAR_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by time
    @Test
    public void test_nominal_case_time_filter() throws InterruptedException {

        // Given - setting 9:00 time range selected
        List<MeetingTimeItem> meetingTimeItemList = getDefaultMeetingTimeItemList();
        meetingTimeItemList.get(1).setChecked(true);
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(filterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "1 " + MOCKED_SINGULAR_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by room and time
    @Test
    public void test_nominal_case_room_and_time_filter() throws InterruptedException {

        // Given - setting mushroom room selected
        List<MeetingRoomItem> meetingRoomItemList = getDefaultMeetingRoomItems();
        meetingRoomItemList.get(2).setChecked(true);
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);

        // setting 9:00 time range selected
        List<MeetingTimeItem> meetingTimeItemList = getDefaultMeetingTimeItemList();
        meetingTimeItemList.get(1).setChecked(true);
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(filterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "0 " + MOCKED_SINGULAR_STRING);
    }

    // region IN
    private List<MeetingRoomItem> getDefaultMeetingRoomItems() {
        List<MeetingRoomItem> meetingRoomItemList = new ArrayList<>();
        int id = 0;
        meetingRoomItemList.add(new MeetingRoomItem("Flower", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Leaf", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Mushroom", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Coin", id));
        return meetingRoomItemList;
    }

    private List<MeetingTimeItem> getDefaultMeetingTimeItemList(){
        return TimeGen.getAvailableTimes(8,18);
    }

    private void wireUpRoomRepository() {
        Mockito.doReturn(new Room("Flower", R.drawable.ic_room_flower))
                .when(roomsRepository)
                .getRoomByName("Flower");
        Mockito.doReturn(new Room("Leaf", R.drawable.ic_room_leaf))
                .when(roomsRepository)
                .getRoomByName("Leaf");
        Mockito.doReturn(new Room("Mushroom", R.drawable.ic_room_mushroom))
                .when(roomsRepository)
                .getRoomByName("Mushroom");
    }

    private void wireUpRoomFilter(){
        meetingRoomItemListMutableLiveData.setValue(getDefaultMeetingRoomItems());
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();
    }

    private void wireUpTimeFilter(){
        meetingTimeItemListMutableLiveData.setValue(getDefaultMeetingTimeItemList());
        Mockito.doReturn(meetingTimeItemListMutableLiveData)
                .when(timeFilterRepository)
                .getMeetingTimeItemListLiveData();
    }

    private void wireUpMeetingRepository() {
        MutableLiveData<List<Meeting>> meetingListMutableLiveData = new MutableLiveData<>();
        meetingListMutableLiveData.setValue(FakeMeetingsGen.generateFakeMeetingList(roomsRepository));
        Mockito.doReturn(meetingListMutableLiveData)
                .when(meetingsRepository).getMeetings();
    }

    // endregion
}
