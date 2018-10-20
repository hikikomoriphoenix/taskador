package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.LoginActivity;

/**
 * Handles background tasks for login screen.
 */
public interface LoginBackgroundTasker extends BackgroundTasker<LoginActivity> {
    /**
     * Log in to account using credentials inputted by user.
     *
     * @param username username of account
     * @param password password to account
     */
    void login(String username, String password);
}
