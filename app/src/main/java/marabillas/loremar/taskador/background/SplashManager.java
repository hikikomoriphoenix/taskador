package marabillas.loremar.taskador.background;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;

public class SplashManager extends BackgroundTaskManager implements SplashBackgroundTasker {
    private SplashActivity splashActivity;
    private Runnable nextScreen;

    private boolean nextScreenTimerFinished;
    private boolean backgroundTaskFinished;

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
                    String currentAccountUsername = prefs.getString("current_account_username", null);

                    if (currentAccountUsername == null) {
                        nextScreen = new Login();
                        backgroundTaskFinished();
                        nextScreen();
                    }
                }
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
            nextScreen.run();
        }
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
}
