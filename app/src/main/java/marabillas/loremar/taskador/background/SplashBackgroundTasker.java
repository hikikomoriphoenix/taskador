package marabillas.loremar.taskador.background;

import android.os.Bundle;

import marabillas.loremar.taskador.ui.activity.SplashActivity;

public interface SplashBackgroundTasker extends BackgroundTasker<SplashActivity> {
    void startSplashBackground(Bundle input);

    void backgroundTaskFinished();

    void nextScreenTimerFinished();

    void nextScreen();

    enum Action {SIGNUP}
}
