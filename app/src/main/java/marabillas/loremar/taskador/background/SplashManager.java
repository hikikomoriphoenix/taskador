package marabillas.loremar.taskador.background;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import marabillas.loremar.taskador.ConfigKeys;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.SignupTask;
import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;

import static marabillas.loremar.taskador.utils.LogUtils.logError;

public class SplashManager extends BackgroundTaskManager implements SplashBackgroundTasker, SignupTask.ResultHandler {
    private SplashActivity splashActivity;
    private Runnable nextScreen;

    private boolean nextScreenTimerFinished;
    private boolean backgroundTaskFinished;

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
                if (input == null) {
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

    private void signup(final Bundle input) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                splashActivity.setStatusText("Creating New Account...");
                String username = input.getString("username");
                String password = input.getString("password");
                SplashManager.this.username = username;
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
    public void nextScreen() {
        if (nextScreenTimerFinished && backgroundTaskFinished) {
            getHandler().post(nextScreen);
        }
    }

    @Override
    public void newAccountSaved(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = splashActivity.getSharedPreferences("config", 0);
                prefs.edit().putString(ConfigKeys.CURRENT_ACCOUNT_USERNAME, username).apply();
                // TODO Create a new screen congratulating user for new account. Direct to this
                // screen instead of in app.
                nextScreen = new InApp();
                backgroundTaskFinished();
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

    private class Login implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(splashActivity, LoginActivity.class);
            splashActivity.startActivity(intent);
            splashActivity.finish();
            stopSelf();
        }
    }

    private class InApp implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(splashActivity, MainInAppActivity.class);
            splashActivity.startActivity(intent);
            splashActivity.finish();
            stopSelf();
        }
    }
}
