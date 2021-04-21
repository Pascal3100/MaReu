package fr.plopez.mareu.view;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.plopez.mareu.data.GlobalRepository;
import fr.plopez.mareu.data.model.Meeting;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Meeting>> meetings;
    private GlobalRepository repo;

    public MainActivityViewModel(GlobalRepository repo){
        this.repo = repo;
        init();
    }

    public MutableLiveData<List<Meeting>> getMeetings() {
        return meetings;
    }

    private void init(){
        if (meetings != null){
            return;
        }

        meetings = repo.getMeetings();
    }

}
