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

package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;

/**
 * {@link RunnableTask} for checking if username is available and can be used for registering a
 * new account. A username is available if and only if it is unique in the back-end database.
 */
public class CheckUsernameAvailabilityTask extends RunnableTask<CheckUsernameAvailabilityTask
        .ResultHandler> {
    private String username;

    /**
     * @param username username to be checked for availability
     */
    public CheckUsernameAvailabilityTask(String username) {
        this.username = username;

        setRequestUrl(BuildConfig.backend_url + "account/check-username-availability.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        postForm(form);
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.UsernameAvailabilityCheckResultsObtained(message, data);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.BackendFailedToCheckUsernameAbility(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.BackendFailedToCheckUsernameAbility(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedUsernameAvailabilityCheckRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.CheckUsernameAvailabilityTaskIncomplete(message);
        }
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback method invoked when results are obtained after requesting back-end to check
         * if username is available.
         *
         * @param data a JSON data with the following structure:
         *             <pre>{@code
         *                                      {
         *                                          "available":<true or false>
         *                                      }
         *                                     }
         *                                     </pre>
         */
        void UsernameAvailabilityCheckResultsObtained(String message, JSON data);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedUsernameAvailabilityCheckRequest(String message);

        /**
         * Callback when back-end server can't check username's availability due to either a
         * client error or server error.
         */
        void BackendFailedToCheckUsernameAbility(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void CheckUsernameAvailabilityTaskIncomplete(String message);
    }
}
