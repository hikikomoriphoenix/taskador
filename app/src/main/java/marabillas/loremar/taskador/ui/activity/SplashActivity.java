/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.SplashBackgroundTasker;
import marabillas.loremar.taskador.background.SplashManager;
import marabillas.loremar.taskador.ui.components.SplashInterface;
import marabillas.loremar.taskador.ui.view.WaitingDotsView;

/**
 * This is taskador's main launcher activity. It displays a splash screen while taskador is
 * logging in to the server or making transitions from one screen to another.
 */
public class SplashActivity extends BaseActivity implements SplashInterface {
    private Bundle input;
    private SplashBackgroundTasker splashBackgroundTasker;
    private NextScreenTimer nextScreenTimer;
    private TextView statusView;
    private WaitingDotsView dotsView;
    private boolean configurationChanged;
    private boolean noDots; // Flag indicating whether to display or not to display bouncing dots

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        statusView = findViewById(R.id.activity_splash_status_text);

        // Get data passed into this activity. This data will determine what background task to
        // perform on the splash screen. It also contains values necessary to perform the task.
        input = getIntent().getBundleExtra("input");
        // When app is launched through this activity, set this app to support
        // BackgroundTaskManager to perform background tasks for its activities.
        if (input == null) {
            App.getInstance().setBackgroundTaskManagerSupport(true);
        }

        // Check if configuration changed.
        if (savedInstanceState != null) {
            configurationChanged = true;
            noDots = savedInstanceState.getBoolean("no dots");
        }

        // Initiate timer. When countdown completes, allow to continue to next screen.
        int nextScreenTimerDuration = getResources().getInteger(R.integer
                .activity_splash_nextscreentimer_duration);
        nextScreenTimer = new NextScreenTimer(nextScreenTimerDuration, nextScreenTimerDuration);
    }

    @Override
    public void onSetupBackgroundService() {
        setupBackgroundService(SplashManager.class);
    }

    @Override
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        setupBackgroundService(serviceClass, this);
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("no dots", noDots);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dotsView = findViewById(R.id.waitingDotsView);
        if (!noDots) {
            // Display bouncing dots to indicate ongoing process.
            dotsView.setVisibility(View.VISIBLE);

            // dots.animateContinuousWavesOfDots();
            dotsView.animateSingleWavesofDots();

            nextScreenTimer.start();
        } else {
            dotsView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dotsView.stopAnimation();
        dotsView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        splashBackgroundTasker = (SplashBackgroundTasker) backgroundTaskManager;
        splashBackgroundTasker.bindClient(this);

        if (configurationChanged) {
            String status = splashBackgroundTasker.getStatus();
            if (status != null && !noDots) {
                // Try to restore status text.
                setStatusText(status);
            } else {
                // If no dots to display, the following code makes sure to cause
                // onShowStatusFirst to be invoked which displays the status and continue button.
                splashBackgroundTasker.nextScreenTimerFinished();
                splashBackgroundTasker.nextScreen();
            }
        } else {
            splashBackgroundTasker.startSplashBackground(input);
        }
    }

    @Override
    public Context getSplashContext() {
        return this;
    }

    public void setStatusText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusView.setText(text);
            }
        });
    }

    @Override
    public void onShowStatusFirst(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!noDots) {
                    // Hide bouncing dots.
                    dotsView.stopAnimation();
                    dotsView.setVisibility(View.GONE);
                    noDots = true;
                }

                setStatusText(status);

                // Show Continue button that allows continuing to next screen on click.
                Button continueButton = findViewById(R.id.activity_splash_continue_button);
                continueButton.setVisibility(View.VISIBLE);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        splashBackgroundTasker.continueToNextScreen();
                    }
                });
            }
        });
    }

    @Override
    public void switchToAnotherScreen(@NonNull Class<? extends Activity> activityClass, @NonNull BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input) {
        switchScreen(activityClass, backgroundTaskManager, input);
    }

    @Override
    public void finishSplash() {
        finish();
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
