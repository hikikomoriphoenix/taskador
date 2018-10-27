package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * A {@link RunnableTask} used when taskador is launched to verify from the back-end server that the auth
 * token to be used for all network-related tasks is correct and authorized to access the server.
 * In a sense, this task represents an automatic login wherein a verified auth token means the
 * user is logged in.
 * <p>
 * This task requires username and token. The token should be obtained in a separate operation
 * because the {@link marabillas.loremar.taskador.utils.AccountUtils#getAuthToken} method uses
 * the {@link LoginTask} class in which this would conflict with the idea that only one
 * {@link RunnableTask} should be executed at a time.
 * <p>
 * Pass this as an argument to {@link marabillas.loremar.taskador.network.BackEndResponseHandler}'s
 * {@link marabillas.loremar.taskador.network.BackEndResponseHandler#handle} function to get
 * results from {@link marabillas.loremar.taskador.network.BackEndResponse} handling and set a
 * {@link VerifyTokenTask.ResultHandler} to handle these results.
 */
public class VerifyTokenTask extends ReauthenticatingTask<VerifyTokenTask.ResultHandler> {
    private String username;
    private String token;

    /**
     * Creates a {@link VerifyTokenTask}. The auth token needs to be obtained from the Account Manager.
     *
     * @param username account username
     * @param token auth token
     */
    public VerifyTokenTask(String username, String token) {
        super(username);
        this.username = username;
        this.token = token;

        setRequestUrl(BuildConfig.backend_url + "account/verify-token.php");
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
            resultHandler.tokenVerifiedCorrect(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.tokenVerifiedNotCorrect(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backEndFailedToVerifyToken(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedToRequestVerification(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.tokenVerificationIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.verifyToken(getResultHandler(), username, newToken);
    }

    /**
     * Callback interface for auth token verification. Set a class to implement this interface
     * for handling of these results and call {@link RunnableTask#setResultHandler} method to set
     * its instance as the {@link VerifyTokenTask} object's {@link ResultHandler}.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback method when back-end confirmed that submitted token is indeed correct and
         * authorized to acces the server.
         */
        void tokenVerifiedCorrect(String message);

        /**
         * Callback method when {@link IOException} occured while requesting verification to the back-end.
         */
        void failedToRequestVerification(String message);

        /**
         * Callback method when back-end has determined that submitted token is not correct.
         */
        void tokenVerifiedNotCorrect(String message);

        /**
         * Callback method when some server-side error occurred during auth token verification.
         */
        void backEndFailedToVerifyToken(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void tokenVerificationIncomplete(String message);
    }
}
