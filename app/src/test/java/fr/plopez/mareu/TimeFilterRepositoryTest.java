package fr.plopez.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import fr.plopez.mareu.data.TimeFilterRepository;
import fr.plopez.mareu.utils.LiveDataTestUtils;
import fr.plopez.mareu.view.model.MeetingTimeItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TimeFilterRepositoryTest {

    // Constants
    private static final int NOMINAL_NUMBER_OF_TIME_ITEMS = 11;

    // rules
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // Mocks


    private TimeFilterRepository timeFilterRepository;

    @Before
    public void setUp() {
        timeFilterRepository = new TimeFilterRepository();
    }

    // Test that the repo is correctly initialized
    @Test
    public void get_time_items_list_test() throws InterruptedException {

        // Given

        // When
        List<MeetingTimeItem> result = LiveDataTestUtils
                .getOrAwaitValue(timeFilterRepository.getMeetingTimeItemListLiveData());

        // Then
        assertEquals(result.size(),NOMINAL_NUMBER_OF_TIME_ITEMS);
    }

    // Test that the repo is correctly updated
    @Test
    public void get_updated_items_list_test() throws InterruptedException {

        // Given
        List<MeetingTimeItem> initialList = LiveDataTestUtils
                .getOrAwaitValue(timeFilterRepository.getMeetingTimeItemListLiveData());

        initialList.get(0).setChecked(true);

        timeFilterRepository.updateMeetingTimeItemList(initialList);


        // When
        List<MeetingTimeItem> result = LiveDataTestUtils
                .getOrAwaitValue(timeFilterRepository.getMeetingTimeItemListLiveData());

        // Then
        assertTrue(result.get(0).isChecked());
    }



}
