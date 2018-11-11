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

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;

import static marabillas.loremar.taskador.utils.AccountUtils.createAccount;
import static marabillas.loremar.taskador.utils.LogUtils.logError;

/**
 * A {@link RunnableTask} specifically used for registering a new account. As a {@link Runnable}, it
 * executes the POST request for signing up a new account. This can be set as a listener to
 * signup status results and receive auth token as well. Set a {@link SignupTask.ResultHandler} to handle
 * these results.
 */
public class SignupTask extends RunnableTask<SignupTask.ResultHandler> {
    private String username;
    private String password;

    /**
     * Creates a {@link SignupTask} object with the desired account login credentials.
     *
     * @param username account username
     * @param password account password
     */
    public SignupTask(String username, String password) {
        this.username = username;
        this.password = password;

        setRequestUrl(BuildConfig.backend_url + "account/signup.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);
        postForm(form);
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        // Get the token sent from the back end server and save it in the device paired with its
        // corresponding account.
        try {
            String token = data.getString("token");
            createAccount(App.getInstance().getApplicationContext(), username, password, token);
        } catch (FailedToGetFieldException e) {
            logError("Couldn't add account to Account Manager. No available auth token to set: ");
        }

        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.newAccountSaved(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedToSubmitNewAccount(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.signupTaskIncomplete(message);
        }
    }

    /**
     * Callback interface for signup results. Set a class to implement this interface for handling
     * of these results and call {@link RunnableTask#setResultHandler} method to set its instance
     * as the {@link SignupTask} object's {@link ResultHandler}.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback method when account is successfully registered to the back-end database.
         */
        void newAccountSaved(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back end
         * server.
         */
        void failedToSubmitNewAccount(String message);

        /**
         * Callback method when account can't be registered to database due to various causes
         * which can either be client-side like invalid username and password, or server-side
         * like back-end unable to connect to database.
         */
        void backEndUnableToSaveNewAccount(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void signupTaskIncomplete(String message);
    }
}
