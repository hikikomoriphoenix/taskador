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

import marabillas.loremar.taskador.ui.activity.SignupActivity;
import marabillas.loremar.taskador.ui.components.LoginInterface;

/**
 * Handles background tasks for login screen.
 */
public interface LoginBackgroundTasker extends BackgroundTasker<LoginInterface> {
    /**
     * Log in to account using credentials inputted by user.
     *
     * @param username username of account
     * @param password password to account
     */
    void login(String username, String password);

    /**
     * Start {@link SignupActivity}
     */
    void switchToSignupScreen();
}
