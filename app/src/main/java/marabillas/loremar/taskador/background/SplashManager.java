package marabillas.loremar.taskador.background;

import android.content.SharedPreferences;
import android.os.Bundle;

import marabillas.loremar.taskador.ConfigKeys;
import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.SignupTask;
import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;

import static marabillas.loremar.taskador.utils.LogUtils.logError;

/**
 * Service that handles background tasks for splash screen.
 */
public class SplashManager extends BackgroundTaskManager implements SplashBackgroundTasker, SignupTask.ResultHandler {
    private SplashActivity splashActivity;
    private Runnable nextScreen;

    private boolean nextScreenTimerFinished;
    private boolean backgroundTaskFinished;
    private boolean showStatusFirst;
    private String status;

    private String username;

    @Override
    public void bindClient(SplashActivity client) {
        splashActivity = client;
    }

    @Override
    public SplashActivity getClient() {
        return splashActivity;
    }

    @Override
    public void startSplashBackground(final Bundle input) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                // Identify the background task to perform using value from input with "action"
                // key. If no task set, then attempt to log in.
                if (input == null) {
                    // Get current account to log in. If not set, proceed to login screen.
                    SharedPreferences prefs = getSharedPreferences("config", 0);
                    String currentAccountUsername = prefs.getString(ConfigKeys
                            .CURRENT_ACCOUNT_USERNAME, null);

                    if (currentAccountUsername == null) {
                        nextScreen = new Login();
                        backgroundTaskFinished();
                        nextScreen();
                    }
                } else if (input.getInt("action") == Action.SIGNUP.ordinal()) {
                    signup(input);
                }
            }
        });
    }

    /**
     * Perform a signup task.
     *
     * @param input values needed for signup task.
     */
    private void signup(final Bundle input) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                String text = getString(R.string.activity_splash_status_creating_new_account);
                splashActivity.setStatusText(text);

                // Prepare values for signup task
                String username = input.getString("username");
                String password = input.getString("password");
                SplashManager.this.username = username;

                // Execute a network task requesting back-end to create new account
                BackEndAPICallTasker.getInstance().signup(SplashManager.this, username, password);
            }
        });
    }

    @Override
    public void backgroundTaskFinished() {
        backgroundTaskFinished = true;
    }

    @Override
    public void nextScreenTimerFinished() {
        nextScreenTimerFinished = true;
    }

    @Override
    public void showStatusFirst(String status) {
        showStatusFirst = true;
        this.status = status;
    }

    @Override
    public void nextScreen() {
        if (nextScreenTimerFinished && backgroundTaskFinished) {
            if (!showStatusFirst) {
                getHandler().post(nextScreen);
            } else {
                splashActivity.onShowStatusFirst(status);
            }
        }
    }

    @Override
    public void continueToNextScreen() {
        getHandler().post(nextScreen);
    }

    @Override
    public void newAccountSaved(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                // Set newly created account as current account.
                SharedPreferences prefs = splashActivity.getSharedPreferences("config", 0);
                prefs.edit().putString(ConfigKeys.CURRENT_ACCOUNT_USERNAME, username).apply();

                // Set to continue to in-app screen but show status first before continuing.
                nextScreen = new InApp();
                backgroundTaskFinished();
                String status = getString(R.string.activity_splash_status_new_account_created);
                showStatusFirst(status);
                nextScreen();
            }
        });
    }

    @Override
    public void failedToSubmitNewAccount(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }

    @Override
    public void backEndUnableToSaveNewAccount(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }

    @Override
    public void signupTaskIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }

    /**
     * Runnable to continue to login screen.
     */
    private class Login implements Runnable {
        @Override
        public void run() {
            splashActivity.switchScreen(LoginActivity.class, SplashManager.this, null);
        }
    }

    /**
     * Runnable to continue to in-app screen.
     */
    private class InApp implements Runnable {
        @Override
        public void run() {
            splashActivity.switchScreen(MainInAppActivity.class, SplashManager.this, null);
        }
    }
}
