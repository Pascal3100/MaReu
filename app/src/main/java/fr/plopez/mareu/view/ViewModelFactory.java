package fr.plopez.mareu.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.view.add.AddMeetingViewModel;
import fr.plopez.mareu.view.main.MainActivityViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    private final MeetingsRepository meetingsRepository = MeetingsRepository.getMeetingsRepositoryInstance();
    private final RoomsRepository roomsRepository = RoomsRepository.getRoomsRepositoryInstance();

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(meetingsRepository, roomsRepository);
        } else if (modelClass.isAssignableFrom(AddMeetingViewModel.class)) {
            return (T) new AddMeetingViewModel(meetingsRepository, roomsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
