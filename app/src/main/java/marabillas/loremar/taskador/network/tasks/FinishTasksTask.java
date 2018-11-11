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

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for setting some selected tasks as finished which will be saved into account.
 * Call run to execute task.
 */
public class FinishTasksTask extends ReauthenticatingTask<FinishTasksTask.ResultHandler> {
    private String username;
    private String token;
    private IdTaskPair[] idTaskEntries;

    /**
     * @param username      account username
     * @param token         auth token
     * @param idTaskEntries an array of entries containing the tasks and their corresponding ids.
     */
    public FinishTasksTask(String username, String token, IdTaskPair[] idTaskEntries) {
        super(username);
        this.username = username;
        this.token = token;
        this.idTaskEntries = idTaskEntries;

        setRequestUrl(BuildConfig.backend_url + "tasks/finish-tasks.php");
    }

    @Override
    public void run() {
        try {
            // Create JSONTree array for idTaskEntries
            JSONTree[] idTasksJson = new JSONTree[idTaskEntries.length];
            for (int i = 0; i < idTaskEntries.length; ++i) {
                idTasksJson[i] = new JSONTree()
                        .put("task", idTaskEntries[i].task)
                        .put("id", idTaskEntries[i].id);
            }

            // Convert fields to JSON
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("tasks", idTasksJson)
                    .toString();

            // Send request
            postJSON(json);
        } catch (JSONTreeException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.finishTasksTaskFailedToPrepareJSONData(e.getMessage());
            }
            BackEndAPICallTasker.getInstance().cancelTask();
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.finishedTasksSavedSuccessfully(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToFinishTask(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToFinishTask(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.finishTasks(getResultHandler(), username, newToken, idTaskEntries);
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedFinishTasksRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.finishTasksTaskIncomplete(message);
        }
    }

    /**
     * Callback interface after validating and handling back-end's response.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback when selected tasks are saved as finished in account
         */
        void finishedTasksSavedSuccessfully(String message);

        /**
         * Callback when unable to construct JSON data
         */
        void finishTasksTaskFailedToPrepareJSONData(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedFinishTasksRequest(String message);

        /**
         * Callback method when client or server error occured while finishing tasks. If it was
         * due to server error, some inconsistencies might have been introduced to account.
         */
        void backendUnableToFinishTask(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void finishTasksTaskIncomplete(String message);
    }
}
