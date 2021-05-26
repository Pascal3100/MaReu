package fr.plopez.mareu;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
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
import fr.plopez.mareu.view.main.filter_fragment_dialog.MainActivityFilterDialogFragmentViewModel;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityFilterDialogFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    public MeetingsRepository meetingsRepository;
    public RoomsRepository roomsRepository;
    private RoomFilterRepository roomFilterRepository;
    private TimeFilterRepository timeFilterRepository;
    private MainActivityFilterDialogFragmentViewModel mainActivityFilterDialogFragmentViewModel;
    private List<MeetingRoomItem> meetingRoomItemList;
    private List<MeetingTimeItem> meetingTimeItemList;
    private final MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();

    private final String MOCKED_SINGULAR_STRING = "MOCKED_SINGULAR_STRING";
    private final String MOCKED_PLURAL_STRING = "MOCKED_PLURAL_STRING";

    @Mock
    Application mockContext;


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
/*
        Mockito.doReturn(new Room("Coin", R.drawable.ic_room_coin))
                .when(roomsRepository)
                .getRoomByName("Coin");
        Mockito.doReturn(Arrays.asList("Flower", "Leaf", "Mushroom", "Coin"))
                .when(roomsRepository)
                .getRoomsNames();
*/

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

        when(mockContext.getString(R.string.filter_text_message_singular))
                .thenReturn(MOCKED_SINGULAR_STRING);
        when(mockContext.getString(R.string.filter_text_message_plural))
                .thenReturn(MOCKED_PLURAL_STRING);
    }

    // Test that list of meetings are correctly not filtered and counted
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

        // Instantiate main Activity Dialog Fragment View Model
        mainActivityFilterDialogFragmentViewModel = new MainActivityFilterDialogFragmentViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository,
                mockContext);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityFilterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "3 "+MOCKED_PLURAL_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by room
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

        // Instantiate main Activity Dialog Fragment View Model
        mainActivityFilterDialogFragmentViewModel = new MainActivityFilterDialogFragmentViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository,
                mockContext);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityFilterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "1 "+MOCKED_SINGULAR_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by time
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

        // Instantiate main Activity Dialog Fragment View Model
        mainActivityFilterDialogFragmentViewModel = new MainActivityFilterDialogFragmentViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository,
                mockContext);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityFilterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "1 "+MOCKED_SINGULAR_STRING);
    }

    // Test that list of meetings are correctly filtered and counted by room and time
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

        // Instantiate main Activity Dialog Fragment View Model
        mainActivityFilterDialogFragmentViewModel = new MainActivityFilterDialogFragmentViewModel(
                meetingsRepository,
                roomFilterRepository,
                timeFilterRepository,
                mockContext);

        // When
        String result = LiveDataTestUtils
                .getOrAwaitValue(mainActivityFilterDialogFragmentViewModel.getFilterDialogDynamicTextLiveData());

        // Then
        assertEquals(result, "0 "+MOCKED_SINGULAR_STRING);
    }
}
