package fr.plopez.mareu.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.plopez.mareu.data.GlobalRepository;
import fr.plopez.mareu.data.model.Meeting;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Meeting>> meetings;
    private GlobalRepository globalRepository;

    public MainActivityViewModel(GlobalRepository globalRepository){
        this.globalRepository = globalRepository;
        init();
    }

    public MutableLiveData<List<Meeting>> getMeetings() {
        return meetings;
    }

    private void init(){
        if (meetings != null){
            return;
        }
        meetings = globalRepository.getMeetings();
    }

    // Add a new meeting
    public void addMeeting(@NonNull Meeting meeting){
        globalRepository.addMeeting(meeting);
        meetings.setValue(globalRepository.getMeetings().getValue());
    }

    // delete selected meeting
    public void deleteMeeting(@NonNull Meeting meeting){
        globalRepository.deleteMeeting(meeting);
        meetings.setValue(globalRepository.getMeetings().getValue());
    }

    public List<String> getRoomsNames() {
        return globalRepository.getRooms();
    }
}
