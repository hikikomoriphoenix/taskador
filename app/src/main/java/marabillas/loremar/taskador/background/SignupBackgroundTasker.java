package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.SignupActivity;

/**
 * Handles background tasks for signup screen.
 */
public interface SignupBackgroundTasker extends BackgroundTasker<SignupActivity> {
    /**
     * Check if username is unique and can be used for creating new account.
     *
     * @param username username to check
     */
    void checkUsernameAvailability(String username);

    /**
     * Stop any ongoing request for checking username availability.
     */
    void cancelUsernameAvailabilityCheck();

    /**
     * Submit new account for signup with the desired credentials.
     *
     * @param username desired username for new account
     * @param password desired password for new account
     */
    void submitNewAccount(String username, String password);
}
