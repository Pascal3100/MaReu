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
import fr.plopez.mareu.view.main.MainActivityViewModel;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;
import fr.plopez.mareu.view.model.MeetingViewState;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {

    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // mocks
    @Mock
    public MeetingsRepository meetingsRepository;
    @Mock
    public RoomsRepository roomsRepository;
    @Mock
    private RoomFilterRepository roomFilterRepository;
    @Mock
    private TimeFilterRepository timeFilterRepository;

    // class variables
    private MainActivityViewModel mainActivityViewModel;
    private final MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {

        wireUpRoomRepository();

        wireUpRoomFilter();

        wireUpTimeFilter();

        wireUpMeetingRepository();

        // Instantiate main Activity View Model
        mainActivityViewModel = new MainActivityViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository);
    }


    // Test that list of meetings are correctly transformed into meetingViewState objects list
    @Test
    public void test_nominal_case_no_filter() throws InterruptedException {

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 3);
    }

    // Test that list of meetings are correctly filtered by room
    @Test
    public void test_nominal_case_room_filter() throws InterruptedException {

        // Given - setting mushroom room selected
        List<MeetingRoomItem> meetingRoomItemList = getDefaultMeetingRoomItems();
        meetingRoomItemList.get(2).setChecked(true);
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 1);
    }

    // Test that list of meetings are correctly filtered by time
    @Test
    public void test_nominal_case_time_filter() throws InterruptedException {

        // Given - setting 9:00 time range selected
        List<MeetingTimeItem> meetingTimeItemList = getDefaultMeetingTimeItemList();
        meetingTimeItemList.get(1).setChecked(true);
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 1);
    }

    // Test that list of meetings are correctly filtered by room and time
    @Test
    public void test_nominal_case_room_and_time_filter() throws InterruptedException {

        // Given - setting mushroom room selected
        List<MeetingRoomItem> meetingRoomItemList = getDefaultMeetingRoomItems();
        meetingRoomItemList.get(2).setChecked(true);
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);
        // Given - setting 9:00 time range selected
        List<MeetingTimeItem> meetingTimeItemList = getDefaultMeetingTimeItemList();
        meetingTimeItemList.get(1).setChecked(true);
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 0);
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

    private List<MeetingTimeItem> getDefaultMeetingTimeItemList() {
        return TimeGen.getAvailableTimes(8, 18);
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

    private void wireUpRoomFilter() {
        meetingRoomItemListMutableLiveData.setValue(getDefaultMeetingRoomItems());
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();
    }

    private void wireUpTimeFilter() {
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