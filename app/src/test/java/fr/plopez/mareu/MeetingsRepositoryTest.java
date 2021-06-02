package fr.plopez.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.utils.LiveDataTestUtils;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MeetingsRepositoryTest {

    // constants
    private final String CORRECT_STRING_INPUT = "CORRECT_STRING_INPUT";
    private final String CORRECT_ROOM_INPUT = "Mushroom";
    private final int FAKE_MEETING_ID = 1;
    private final Room FAKE_MEETING_ROOM = new Room(CORRECT_ROOM_INPUT, R.drawable.ic_room_mushroom);
    private final List<String> FAKE_MEETING_EMAIL_LIST = Arrays.asList("pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com");
    private final Meeting FAKE_MEETING_OBJECT = new Meeting(
                          FAKE_MEETING_ID,
                          CORRECT_STRING_INPUT,
                          CORRECT_STRING_INPUT,
                          FAKE_MEETING_ROOM,
                          FAKE_MEETING_EMAIL_LIST);

    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // mocks


    // class variables
    private MeetingsRepository meetingsRepository;
    private int numberOfMeetings = 0;


    @Before
    public void setUp() {
        meetingsRepository = MeetingsRepository.getMeetingsRepositoryInstance();
        numberOfMeetings = meetingsRepository.getMeetings().getValue().size();
    }

    @Test
    public void add_meeting_nominal_case_test() throws InterruptedException {

        // Given
        meetingsRepository.addMeeting(FAKE_MEETING_OBJECT);

        // When
        List<Meeting> result = LiveDataTestUtils
                .getOrAwaitValue(meetingsRepository.getMeetings());

        // Then
        assertEquals(numberOfMeetings +1, result.size());
    }

    @Test
    public void remove_meeting_nominal_case_test() throws InterruptedException {

        // Given
        meetingsRepository.deleteMeeting(meetingsRepository.getMeetings().getValue().get(0).getId());

        // When
        List<Meeting> result = LiveDataTestUtils
                .getOrAwaitValue(meetingsRepository.getMeetings());

        // Then
        assertEquals(numberOfMeetings -1, result.size());
    }

    @Test
    public void remove_meeting_bad_id_case_test() throws InterruptedException {
        // Given
        Random rand = new Random();
        int int_random = rand.nextInt(100) + 10;
        meetingsRepository.deleteMeeting(int_random);

        // When
        List<Meeting> result = LiveDataTestUtils
                .getOrAwaitValue(meetingsRepository.getMeetings());

        // Then
        assertEquals(numberOfMeetings, result.size());
    }

    @After
    public void deleteRepository(){
        meetingsRepository = null;
        System.gc();
    }
}
