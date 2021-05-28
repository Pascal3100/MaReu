package fr.plopez.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    public MeetingsRepository meetingsRepository;
    public RoomsRepository roomsRepository;
    private RoomFilterRepository roomFilterRepository;
    private TimeFilterRepository timeFilterRepository;
    private MainActivityViewModel mainActivityViewModel;
    private List<MeetingRoomItem> meetingRoomItemList;
    private List<MeetingTimeItem> meetingTimeItemList;
    private final MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {

        // Mocking the rooms repository
        roomsRepository = Mockito.mock(RoomsRepository.class);
        Mockito.doReturn(new Room("Flower", R.drawable.ic_room_flower))
                .when(roomsRepository)
                .getRoomByName("Flower");
        Mockito.doReturn(new Room("Leaf", R.drawable.ic_room_leaf))
                .when(roomsRepository)
                .getRoomByName("Leaf");
        Mockito.doReturn(new Room("Mushroom", R.drawable.ic_room_mushroom))
                .when(roomsRepository)
                .getRoomByName("Mushroom");

        // Mocking the meetings repository
        meetingsRepository = Mockito.mock(MeetingsRepository.class);
        MutableLiveData<List<Meeting>> meetingListMutableLiveData = new MutableLiveData<>();
        meetingListMutableLiveData.setValue(FakeMeetingsGen.generateFakeMeetingList(roomsRepository));
        Mockito.doReturn(meetingListMutableLiveData)
                .when(meetingsRepository).getMeetings();

        // Mocking the rooms filter repository
        roomFilterRepository = Mockito.mock(RoomFilterRepository.class);
        meetingRoomItemList = new ArrayList<>();
        int id = 0;
        meetingRoomItemList.add(new MeetingRoomItem("Flower", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Leaf", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Mushroom", id++));
        meetingRoomItemList.add(new MeetingRoomItem("Coin", id++));

        // Mocking the time filter repository
        timeFilterRepository = Mockito.mock(TimeFilterRepository.class);
        meetingTimeItemList = TimeGen.getAvailableTimes(8, 18);

    }

    // Test that list of meetings are correctly transformed into meetingViewState objects list
    @Test
    public void test_nominal_case_no_filter() throws InterruptedException {

        // Given
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();

        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);
        Mockito.doReturn(meetingTimeItemListMutableLiveData)
                .when(timeFilterRepository)
                .getMeetingTimeItemListLiveData();

        // Instantiate main Activity View Model
        mainActivityViewModel = new MainActivityViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 3);
    }

    // Test that list of meetings are correctly filtered by room
    @Test
    public void test_nominal_case_room_filter() throws InterruptedException {

        // Given
        // setting mushroom room selected
        meetingRoomItemList.get(2).setChecked(true);

        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();

        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);
        Mockito.doReturn(meetingTimeItemListMutableLiveData)
                .when(timeFilterRepository)
                .getMeetingTimeItemListLiveData();

        // Instantiate main Activity View Model
        mainActivityViewModel = new MainActivityViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 1);
    }

    // Test that list of meetings are correctly filtered by time
    @Test
    public void test_nominal_case_time_filter() throws InterruptedException {

        // Given

        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();

        // setting 9:00 time range selected
        meetingTimeItemList.get(1).setChecked(true);

        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);
        Mockito.doReturn(meetingTimeItemListMutableLiveData)
                .when(timeFilterRepository)
                .getMeetingTimeItemListLiveData();

        // Instantiate main Activity View Model
        mainActivityViewModel = new MainActivityViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 1);
    }

    // Test that list of meetings are correctly filtered by room and time
    @Test
    public void test_nominal_case_room_and_time_filter() throws InterruptedException {

        // Given
        // setting mushroom room selected
        meetingRoomItemList.get(2).setChecked(true);

        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemList);
        Mockito.doReturn(meetingRoomItemListMutableLiveData)
                .when(roomFilterRepository)
                .getMeetingRoomItemListLiveData();

        // setting 9:00 time range selected
        meetingTimeItemList.get(1).setChecked(true);

        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemList);
        Mockito.doReturn(meetingTimeItemListMutableLiveData)
                .when(timeFilterRepository)
                .getMeetingTimeItemListLiveData();

        // Instantiate main Activity View Model
        mainActivityViewModel = new MainActivityViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository);

        // When
        List<MeetingViewState> result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityViewModel.getMainActivityViewStatesLiveData());

        // Then
        assertEquals(result.size(), 0);    }
}