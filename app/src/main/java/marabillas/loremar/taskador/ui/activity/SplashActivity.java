package marabillas.loremar.taskador.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.SplashBackgroundTasker;
import marabillas.loremar.taskador.background.SplashManager;
import marabillas.loremar.taskador.ui.view.WaitingDotsView;

/**
 * This is taskador's main launcher activity. It displays a splash screen while taskador is
 * logging in to the server or making transitions from one screen to another.
 */
public class SplashActivity extends BaseActivity {
    private Bundle input;
    private SplashBackgroundTasker splashBackgroundTasker;
    private NextScreenTimer nextScreenTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        input = getIntent().getExtras();

        int nextScreenTimerDuration = getResources().getInteger(R.integer
                .activity_splash_nextscreentimer_duration);
        nextScreenTimer = new NextScreenTimer(nextScreenTimerDuration, nextScreenTimerDuration);

        if (input == null) {
            setupBackgroundService(SplashManager.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        WaitingDotsView dots = findViewById(R.id.waitingDotsView);
        // dots.animateContinuousWavesOfDots();
        dots.animateSingleWavesofDots();

        nextScreenTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        WaitingDotsView dots = findViewById(R.id.waitingDotsView);
        dots.stopAnimation();
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {

    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        splashBackgroundTasker = (SplashBackgroundTasker) backgroundTaskManager;
        splashBackgroundTasker.bindClient(this);

        splashBackgroundTasker.startSplashBackground(input);
    }

    private class NextScreenTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        NextScreenTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            splashBackgroundTasker.nextScreenTimerFinished();
            splashBackgroundTasker.nextScreen();
        }
    }
}
