package fr.plopez.mareu.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.plopez.mareu.data.GlobalRepository;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private static MainActivityViewModelFactory factory;
    private GlobalRepository repo = GlobalRepository.getInstance();

    public static MainActivityViewModelFactory getInstance(){
        if (factory == null) {
            synchronized (MainActivityViewModelFactory.class) {
                if (factory == null) {
                    factory = new MainActivityViewModelFactory();
                }
            }
        }
        return factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(repo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
