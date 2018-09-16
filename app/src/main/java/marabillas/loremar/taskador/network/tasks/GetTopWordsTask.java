package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * RunnableTask for getting the most frequently used words in task.
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
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedGetTopWordsRequest(e.getMessage());
            }
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
         * Callback method when IOException occur while sending POST request to the back-end server.
         */
        void failedGetTopWordsRequest(String message);

        /**
         * Callback for client or server error on getting top words
         */
        void backendUnableToGetTopWords(String message);

        /**
         * Callback method when InterruptedException or ExecutionException is encountered.
         */
        void getTopWordsTaskIncomplete(String message);
    }
}
