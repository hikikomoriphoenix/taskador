package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.SignupActivity;

public interface SignupBackgroundTasker extends BackgroundTasker<SignupActivity> {
    void checkUsernameAvailability(String username);

    void cancelUsernameAvailabilityCheck();

    void submitNewAccount(String username, String password);
}
