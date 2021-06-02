package fr.plopez.mareu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Room;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class RoomsRepositoryTest {

    // constants

    // rules

    // mocks

    // class variables
    private final RoomsRepository roomsRepository = RoomsRepository.getRoomsRepositoryInstance();

    @Test
    public void get_room_object_by_name_nominal_case(){
        String CORRECT_ROOM_INPUT = "Mushroom";

        Room result = roomsRepository.getRoomByName(CORRECT_ROOM_INPUT);

        assertEquals(result.getName(), CORRECT_ROOM_INPUT.toLowerCase());
    }

    @Test
    public void get_room_object_by_name_wrong_name_case(){
        String INCORRECT_ROOM_INPUT = "INCORRECT_ROOM_INPUT";

        Room result = roomsRepository.getRoomByName(INCORRECT_ROOM_INPUT);

        assertNull(result);
    }
}
