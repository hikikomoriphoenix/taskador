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

/**
 * Handles background tasks for signup screen.
 */
public interface SignupBackgroundTasker extends BackgroundTasker<SignupActivity> {
    /**
     * Check if username is unique and can be used for creating new account.
     *
     * @param username username to check
     */
    void checkUsernameAvailability(String username);

    /**
     * Stop any ongoing request for checking username availability.
     */
    void cancelUsernameAvailabilityCheck();

    /**
     * Submit new account for signup with the desired credentials.
     *
     * @param username desired username for new account
     * @param password desired password for new account
     */
    void submitNewAccount(String username, String password);
}
