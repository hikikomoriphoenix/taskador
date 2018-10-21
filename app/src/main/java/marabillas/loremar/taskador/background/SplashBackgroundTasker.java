package marabillas.loremar.taskador.background;

import android.os.Bundle;

import marabillas.loremar.taskador.ui.activity.SplashActivity;

public interface SplashBackgroundTasker extends BackgroundTasker<SplashActivity> {
    void startSplashBackground(Bundle input);

    void backgroundTaskFinished();

    void nextScreenTimerFinished();

    void showSatusFirst(int statusTextResId);

    void nextScreen();

    void continueToNextScreen();

    enum Action {SIGNUP}
}
