package fr.plopez.mareu;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class UserViewModelTest {

    private static final String EXPECTED_SENTENCE = "EXPECTED_SENTENCE";
    private static final int EXPECTED_AGE = 30;
    private static final String EXPECTED_NAME = "EXPECTED_NAME";

    @Rule
    public InstantTaskExecutorRule mRule = new InstantTaskExecutorRule();

    private UserRepository mUserRepository;
    private Application mApplication;

    private UserViewModel mViewModel;

    @Before
    public void setUp() {

        mUserRepository = Mockito.mock(UserRepository.class);
        mApplication = Mockito.mock(Application.class);

        Mockito.doReturn(new User(EXPECTED_AGE, EXPECTED_NAME)).when(mUserRepository).getUser();
        Mockito.doReturn(EXPECTED_SENTENCE).when(mApplication).getString(R.string.sentence, EXPECTED_NAME, EXPECTED_AGE);

        mViewModel = new UserViewModel(mApplication, mUserRepository);

    }

    @Test
    public void nominal_case() throws InterruptedException {

        // When
        UserViewState result = LiveDataTestUtils.getOrAwaitValue(mViewModel.getViewState());

        // Then
        assertEquals(EXPECTED_SENTENCE, result.getPresentationSentence());
        Mockito.verify(mUserRepository).getUser();
        Mockito.verifyNoMoreInteractions(mUserRepository);
    }

    @Test
    public void error_case_age_is_unknown() throws InterruptedException {

        // Given
        Mockito.doReturn(new User(null, EXPECTED_NAME)).when(mUserRepository).getUser();
        Mockito.doReturn(EXPECTED_SENTENCE).when(mApplication).getString(R.string.sentence, EXPECTED_NAME, null);

        // When
        UserViewState result = LiveDataTestUtils.getOrAwaitValue(mViewModel.getViewState());

        // Then
        assertEquals(EXPECTED_SENTENCE, result.getPresentationSentence());
    }

}
