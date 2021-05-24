package fr.plopez.mareu;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserViewModel {

    private final Application mApplication;
    private final UserRepository mUserRepository;

    public UserViewModel(Application application, UserRepository userRepository) {
        mApplication = application;
        mUserRepository = userRepository;
    }

    public LiveData<UserViewState> getViewState() {
        MutableLiveData<UserViewState> mutableLiveData = new MutableLiveData<>();

        User user = mUserRepository.getUser();

        int age = user.getAge();

        if (age > 18) {

        } else {

        }

        String sentence = mApplication.getString(R.string.sentence, user.getName(), user.getAge());
        mutableLiveData.setValue(new UserViewState(sentence));
        return mutableLiveData;
    }
}
