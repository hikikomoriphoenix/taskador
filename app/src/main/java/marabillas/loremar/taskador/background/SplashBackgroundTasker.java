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

package marabillas.loremar.taskador.background;

import android.os.Bundle;

import marabillas.loremar.taskador.ui.activity.SplashActivity;

/**
 * Handles background tasks for splash screen.
 */
public interface SplashBackgroundTasker extends BackgroundTasker<SplashActivity> {
    /**
     * Start performing task in the background.
     *
     * @param input values required for the task.
     */
    void startSplashBackground(Bundle input);

    /**
     * Note that background task has finished, allowing continuation to next screen,
     */
    void backgroundTaskFinished();

    /**
     * Note that {@link SplashActivity.NextScreenTimer} completed its countdown, allowing
     * continuation to next screen.
     */
    void nextScreenTimerFinished();

    /**
     * Set screen to show status first before allowing to continue to next screen.
     *
     * @param status text describing status of task.
     */
    void showStatusFirst(String status);

    /**
     * Continue to next screen. Show status first if set so.
     */
    void nextScreen();

    /**
     * Continue to next screen.
     */
    void continueToNextScreen();

    /**
     * An enumeration of background tasks to perform in splash screen.
     */
    enum Action {
        /**
         * Create new account.
         */
        SIGNUP,

        /**
         * Log in to account
         */
        LOGIN,

        /**
         * Log out from account
         */
        LOGOUT
    }
}
