package fr.plopez.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.utils.LiveDataTestUtils;
import fr.plopez.mareu.view.model.MeetingRoomItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RoomFilterRepositoryTest {

    // Constants
    private static final int NOMINAL_NUMBER_OF_ROOM_ITEMS = 1;
    private static final String ROOM_TO_GET = "Mushroom";


    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // Mocks
    @Mock
    private RoomsRepository mockRoomRepository;

    private RoomFilterRepository roomFilterRepository;

    @Before
    public void setUp() {
        Mockito.doReturn(
                Arrays.asList(ROOM_TO_GET)
        ).when(mockRoomRepository).getRoomsNames();

        Mockito.doReturn(
                new Room(ROOM_TO_GET, R.drawable.ic_room_mushroom)
        ).when(mockRoomRepository).getRoomByName(ROOM_TO_GET);

        roomFilterRepository = new RoomFilterRepository(mockRoomRepository);
    }

    // Test that the repo is correctly initialized
    @Test
    public void get_room_items_list_test() throws InterruptedException {

        // Given

        // When
        List<MeetingRoomItem> result = LiveDataTestUtils
                .getOrAwaitValue(roomFilterRepository.getMeetingRoomItemListLiveData());

        // Then
        assertEquals(result.size(),NOMINAL_NUMBER_OF_ROOM_ITEMS);
    }

    // Test that the repo is correctly updated
    @Test
    public void get_updated_items_list_test() throws InterruptedException {

        // Given
        List<MeetingRoomItem> initialList = LiveDataTestUtils
                .getOrAwaitValue(roomFilterRepository.getMeetingRoomItemListLiveData());

        initialList.get(0).setChecked(true);

        roomFilterRepository.updateMeetingRoomItemList(initialList);


        // When
        List<MeetingRoomItem> result = LiveDataTestUtils
                .getOrAwaitValue(roomFilterRepository.getMeetingRoomItemListLiveData());

        // Then
        assertTrue(result.get(0).isChecked());
    }

    // Test if the expected method is called when query rooms names
    @Test
    public void is_correct_method_is_called_for_getting_rooms_names(){

        // Given

        // When
        roomFilterRepository.getRoomsNames();

        // Then
        Mockito.verify(mockRoomRepository, times(2)).getRoomsNames();
    }

    // Test if the expected method is called when query room object by its name
    @Test
    public void is_correct_method_is_called_for_getting_rooms_object_by_name(){

        // Given

        // When
        roomFilterRepository.getRoomByName(ROOM_TO_GET);

        // Then
        Mockito.verify(mockRoomRepository, times(2)).getRoomByName(ROOM_TO_GET);
    }
}
