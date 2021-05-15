package fr.plopez.mareu.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.TimeFilterRepository;
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
    private final RoomFilterRepository roomFilterRepository = RoomFilterRepository.getRoomFilterRepositoryInstance();
    private final TimeFilterRepository timeFilterRepository = TimeFilterRepository.getTimeFilterRepositoryInstance();

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(meetingsRepository, roomFilterRepository, timeFilterRepository);
        } else if (modelClass.isAssignableFrom(AddMeetingViewModel.class)) {
            return (T) new AddMeetingViewModel(meetingsRepository, roomFilterRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
