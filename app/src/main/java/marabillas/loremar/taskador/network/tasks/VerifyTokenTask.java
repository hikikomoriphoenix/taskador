package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;

/**
 * A RunnableTask used when taskador is launched to verify from the back-end server that the auth
 * token to be used for all network-related tasks is correct and authorized to access the server.
 * In a sense, this task represents an automatic login wherein a verified auth token means the
 * user is logged in.
 * <p>
 * This task requires username and token. The token should be obtained in a separate operation
 * because the getAuthToken method uses the LoginTask class in which this would conflict with the
 * idea that only one RunnableTask should be executed at a time.
 * <p>
 * Pass this as an argument to BackEndResponseHandler's handle function to get results from
 * BackEndResponse handling and set a VerifyTokenTask.ResultHandler to handle these results.
 */
public class VerifyTokenTask extends RunnableTask<VerifyTokenTask.ResultHandler> {
    private String username;
    private String token;

    /**
     * Creates a VerifyTokenTask. The auth token needs to be obtained from the Account Manager.
     *
     * @param username account username
     * @param token auth token
     */
    public VerifyTokenTask(String username, String token) {
        this.username = username;
        this.token = token;

        setRequestUrl(BuildConfig.backend_url + "verify-token.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("token", token);

        try {
            postForm(form);
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedToRequestVerification(e.getMessage());
            }
        }

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
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.tokenVerificationIncomplete(message);
        }
    }

    /**
     * Callback interface for auth token verification. Set a class to implement this interface
     * for handling of these results and call setResultHandler method to set its instance as the
     * SignupTask object's ResultHandler.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback method when back-end confirmed that submitted token is indeed correct and
         * authorized to acces the server.
         */
        void tokenVerifiedCorrect(String message);

        /**
         * Callback method when IOException occured while requesting verification to the back-end.
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
         * Callback method when InterruptedException or ExecutionException is encountered.
         */
        void tokenVerificationIncomplete(String message);
    }
}
