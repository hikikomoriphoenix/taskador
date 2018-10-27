package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for setting whether a selected word is to be excluded or not to be
 * excluded from top words.
 */
public class SetExcludedTask extends ReauthenticatingTask<SetExcludedTask.ResultHandler> {
    private String username;
    private String token;
    private String word;
    private int excluded;

    /**
     * @param username account username
     * @param token    auth token
     * @param word     word to exclude from top words
     * @param excluded Set to 1 if word is to be excluded, and 0 if word is to be set as not
     *                 excluded.
     */
    public SetExcludedTask(String username, String token, String word, int excluded) {
        super(username);
        this.username = username;
        this.token = token;
        this.word = word;
        this.excluded = excluded;

        setRequestUrl(BuildConfig.backend_url + "words/set-excluded.php");
    }

    @Override
    public void run() {
        try {
            // Make sure excluded contains an acceptable value
            if (excluded != 0 && excluded != 1) {
                throw new IllegalArgumentException("Value for excluded must be 1 or 0. Value is: " +
                        excluded);
            }
            // Convert fields to JSON
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("word", word)
                    .put("excluded", excluded)
                    .toString();

            // Send request
            postJSON(json);
        } catch (JSONTreeException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.setExcludedFailedToPrepareJSONData(e.getMessage());
            }
            BackEndAPICallTasker.getInstance().cancelTask();
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.selectedWordExcludedSuccessfully(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableUnableToExclude(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableUnableToExclude(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedSetExcludedRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.setExcludedTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.setExcluded(getResultHandler(), username, newToken, word, excluded);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback for successfully setting a word as excluded or not excluded
         */
        void selectedWordExcludedSuccessfully(String message);

        /**
         * Callback when unable to construct JSON data
         */
        void setExcludedFailedToPrepareJSONData(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedSetExcludedRequest(String message);

        /**
         * Callback for client or server error on setting a word as excluded or not excluded from
         * top words
         */
        void backendUnableUnableToExclude(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void setExcludedTaskIncomplete(String message);
    }
}
