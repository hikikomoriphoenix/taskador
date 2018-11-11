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
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for getting the most frequently used words in task.
 */
public class GetTopWordsTask extends ReauthenticatingTask<GetTopWordsTask.ResultHandler> {
    private String username;
    private String token;
    private int numResults;

    /**
     * @param username   account username
     * @param token      auth token
     * @param numResults number of top words to get
     */
    public GetTopWordsTask(String username, String token, int numResults) {
        super(username);
        this.username = username;
        this.token = token;
        this.numResults = numResults;

        setRequestUrl(BuildConfig.backend_url + "words/get-top-words.php");
    }

    @Override
    public void run() {
        try {
            // Convert fields to JSON
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("number_of_results", numResults)
                    .toString();

            // Send request
            postJSON(json);
        } catch (JSONTreeException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.getTopWordsTaskFailedToPrepareJSONData(e.getMessage());
            }
            BackEndAPICallTasker.getInstance().cancelTask();
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.topWordsObtained(message, data);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGetTopWords(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGetTopWords(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedGetTopWordsRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.getTopWordsTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.getTopWords(getResultHandler(), username, newToken, numResults);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback for successfully getting top words
         */
        void topWordsObtained(String message, JSON data);

        /**
         * Callback when unable to construct JSON data
         */
        void getTopWordsTaskFailedToPrepareJSONData(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedGetTopWordsRequest(String message);

        /**
         * Callback for client or server error on getting top words
         */
        void backendUnableToGetTopWords(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void getTopWordsTaskIncomplete(String message);
    }
}
