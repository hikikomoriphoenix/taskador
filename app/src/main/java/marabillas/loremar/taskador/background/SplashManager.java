package marabillas.loremar.taskador.background;

import android.content.SharedPreferences;
import android.os.Bundle;

import marabillas.loremar.taskador.ConfigKeys;
import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.LoginTask;
import marabillas.loremar.taskador.network.tasks.SignupTask;
import marabillas.loremar.taskador.network.tasks.UpdateTaskWordsTask;
import marabillas.loremar.taskador.network.tasks.VerifyTokenTask;
import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.activity.SignupActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;
import marabillas.loremar.taskador.utils.AccountUtils;

import static marabillas.loremar.taskador.utils.AccountUtils.getAuthToken;
import static marabillas.loremar.taskador.utils.LogUtils.log;

/**
 * Service that handles background tasks for splash screen.
 */
public class SplashManager extends BackgroundTaskManager implements SplashBackgroundTasker,
        SignupTask.ResultHandler, VerifyTokenTask.ResultHandler, LoginTask.ResultHandler, UpdateTaskWordsTask.ResultHandler {
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
                    username = currentAccountUsername;

                    if (currentAccountUsername == null) {
                        nextScreen = new Login();
                        backgroundTaskFinished();
                        nextScreen();
                    } else {
                        connect(currentAccountUsername);
                    }
                } else if (input.getInt("action") == Action.SIGNUP.ordinal()) {
                    signup(input);
                } else if (input.getInt("action") == Action.LOGIN.ordinal()) {
                    login(input);
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

    private void connect(final String username) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                String text = getString(R.string.activity_splash_status_connecting);
                splashActivity.setStatusText(text);

                try {
                    String token = getAuthToken(username);
                    BackEndAPICallTasker.getInstance().verifyToken(SplashManager.this, username, token);
                } catch (AccountUtils.GetAuthTokenException e) {
                    onTaskCompleteProceedToShowStatusFirst(new Exit(), e.getMessage());
                }
            }
        });
    }

    private void login(final Bundle input) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                String text = getString(R.string.activity_splash_status_logging);
                splashActivity.setStatusText(text);

                String username = input.getString("username");
                String password = input.getString("password");
                SplashManager.this.username = username;

                BackEndAPICallTasker.getInstance().login(SplashManager.this, username, password);
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

                String status = getString(R.string.activity_splash_status_new_account_created);
                onTaskCompleteProceedToShowStatusFirst(new InApp(), status);
            }
        });
    }

    @Override
    public void failedToSubmitNewAccount(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Signup(), message);
            }
        });
    }

    @Override
    public void backEndUnableToSaveNewAccount(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Signup(), message);
            }
        });
    }

    @Override
    public void signupTaskIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Signup(), message);
            }
        });
    }

    @Override
    public void tokenVerifiedCorrect(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                log("Logged in as " + username);
                updateWordsTableInAccount(new InApp(), new Exit());
            }
        });
    }

    @Override
    public void failedToRequestVerification(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Exit(), message);
            }
        });
    }

    @Override
    public void tokenVerifiedNotCorrect(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Exit(), message);
            }
        });
    }

    @Override
    public void backEndFailedToVerifyToken(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Exit(), message);
            }
        });
    }

    @Override
    public void tokenVerificationIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Exit(), message);
            }
        });
    }

    @Override
    public void loggedInSuccessfuly(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                log("Logged in as " + username);
                updateWordsTableInAccount(new InApp(), new Login());
            }
        });
    }

    @Override
    public void failedToSubmitLogin(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Login(), message);
            }
        });
    }

    @Override
    public void loginDenied(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Login(), message);
            }
        });
    }

    @Override
    public void loginTaskIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(new Login(), message);
            }
        });
    }

    @Override
    public void wordsUpdatedSuccessfully(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                log("Words in account are updated.");
                onTaskCompleteProceedToNextScreen(nextScreen);
            }
        });
    }

    @Override
    public void failedUpdateTaskWordsRequest(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(nextScreen, message);
            }
        });
    }

    @Override
    public void backendUnableToUpdateTaskWords(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(nextScreen, message);
            }
        });
    }

    @Override
    public void updateTaskWordsTaskIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                onTaskCompleteProceedToShowStatusFirst(nextScreen, message);
            }
        });
    }

    private void onTaskCompleteProceedToShowStatusFirst(final Runnable nextScreen, final String
            status) {
        SplashManager.this.nextScreen = nextScreen;
        backgroundTaskFinished();
        showStatusFirst(status);
        nextScreen();
    }

    private void onTaskCompleteProceedToNextScreen(final Runnable nextScreen) {
        SplashManager.this.nextScreen = nextScreen;
        backgroundTaskFinished();
        nextScreen();
    }

    /**
     * @param nextScreen    screen to go to after updating
     * @param failureScreen screen to go to when unable to get auth token
     */
    private void updateWordsTableInAccount(Runnable nextScreen, Runnable failureScreen) {
        try {
            String token = getAuthToken(username);

            String text = getString(R.string.activity_splash_status_updating);
            splashActivity.setStatusText(text);

            this.nextScreen = nextScreen;

            BackEndAPICallTasker.getInstance().updateTaskWords(SplashManager.this, username,
                    token);
        } catch (AccountUtils.GetAuthTokenException e) {
            onTaskCompleteProceedToShowStatusFirst(failureScreen, e.getMessage());
        }
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

    /**
     * Runnable to continue to signup screen.
     */
    private class Signup implements Runnable {
        @Override
        public void run() {
            splashActivity.switchScreen(SignupActivity.class, SplashManager.this, null);
        }
    }

    /**
     * Runnable to exit app.
     */
    private class Exit implements Runnable {
        @Override
        public void run() {
            splashActivity.finish();
            System.exit(0);
        }
    }
}
