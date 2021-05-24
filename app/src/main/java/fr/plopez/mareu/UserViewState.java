package fr.plopez.mareu;

public class UserViewState {

    private final String presentationSentence;

    public UserViewState(String presentationSentence) {
        this.presentationSentence = presentationSentence;
    }

    public String getPresentationSentence() {
        return presentationSentence;
    }
}
