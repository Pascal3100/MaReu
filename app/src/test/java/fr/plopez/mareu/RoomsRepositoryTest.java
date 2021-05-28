package fr.plopez.mareu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Room;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RoomsRepositoryTest {

    private final RoomsRepository roomsRepository = RoomsRepository.getRoomsRepositoryInstance();
    private final String CORRECT_ROOM_INPUT = "Mushroom";
    private final String INCORRECT_ROOM_INPUT = "INCORRECT_ROOM_INPUT";

    @Before
    public void setup(){

    }

    @Test
    public void get_room_object_by_name_nominal_case(){
        Room result = roomsRepository.getRoomByName(CORRECT_ROOM_INPUT);

        assertEquals(result.getName(), CORRECT_ROOM_INPUT.toLowerCase());
    }

    @Test
    public void get_room_object_by_name_wrong_name_case(){
        Room result = roomsRepository.getRoomByName(INCORRECT_ROOM_INPUT);

        assertEquals(result, null);
    }
}
