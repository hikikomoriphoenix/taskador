package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for getting words excluded from top words.
 */
public class GetExcludedWordsTask extends ReauthenticatingTask<GetExcludedWordsTask.ResultHandler> {
    private String username;
    private String token;

    /**
     * @param username account username
     * @param token    auth token
     */
    public GetExcludedWordsTask(String username, String token) {
        super(username);
        this.username = username;
        this.token = token;

        setRequestUrl(BuildConfig.backend_url + "words/get-excluded-words.php");
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
            resultHandler.excludedWordsObtained(message, data);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGiveExcludedWords(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToGiveExcludedWords(message);
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
            resultHandler.getExcludedWordsTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.getExcludedWords(getResultHandler(), username, newToken);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback for successfully getting excluded words from account
         */
        void excludedWordsObtained(String message, JSON data);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedSetExcludedRequest(String message);

        /**
         * Callback for client or server error on getting excluded words
         */
        void backendUnableToGiveExcludedWords(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void getExcludedWordsTaskIncomplete(String message);
    }
}
