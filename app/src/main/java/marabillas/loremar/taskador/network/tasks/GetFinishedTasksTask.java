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
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for getting tasks finished during the current week.
 */
public class GetFinishedTasksTask extends ReauthenticatingTask<GetFinishedTasksTask.ResultHandler> {
    private String username;
    private String token;

    /**
     * @param username account username
     * @param token    auth token
     */
    public GetFinishedTasksTask(String username, String token) {
        super(username);
        this.username = username;
        this.token = token;

        setRequestUrl(BuildConfig.backend_url + "tasks/get-finished-tasks.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("token", token);
        postForm(form);
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.finishedTasksObtained(message, data);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGiveFinishedTasks(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGiveFinishedTasks(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedGetFinishedTasksRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.getFinishedTasksIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.getFinishedTasks(getResultHandler(), username, newToken);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback for successfully getting finished tasks
         */
        void finishedTasksObtained(String message, JSON data);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedGetFinishedTasksRequest(String message);

        /**
         * Callback for client error or server error when getting finished tasks
         */
        void backendUnableToGiveFinishedTasks(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void getFinishedTasksIncomplete(String message);
    }
}
