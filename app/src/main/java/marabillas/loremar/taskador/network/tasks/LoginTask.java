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
 * A RunnableTask specifically used for login. As a Runnable, it executes the POST request for
 * logging in to the server. This can be set as a listener to login status results and receive
 * auth token as well. Set a LoginTask.ResultHandler to handle these results.
 */
public class LoginTask extends RunnableTask<LoginTask.ResultHandler> {
    private String username;
    private String password;

    /**
     * Creates a LoginTask object with account credentials.
     *
     * @param username account username
     * @param password account password
     */
    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;

        setRequestUrl(BuildConfig.backend_url + "account/login.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);

        try {
            postForm(form);
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedToSubmitLogin(e.getMessage());
            }
        }
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
            resultHandler.loggedInSuccessfuly(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.loginDenied(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.loginDenied(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.loginTaskIncomplete(message);
        }
    }

    /**
     * Callback interface for login results. Set a class to implement this interface for handling
     * of these results and call setResultHandler method to set its instance as the LoginTask
     * object's ResultHandler.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback method when login is successful
         */
        void loggedInSuccessfuly(String message);

        /**
         * Callback method when IOException occur while sending POST request to the back end
         * server.
         */
        void failedToSubmitLogin(String message);

        /**
         * Callback method when back end couldn't complete the login process either because the
         * submitted credentials are unacceptable or back end process encountered an error.
         */
        void loginDenied(String message);

        /**
         * Callback method when InterruptedException or ExecutionException is encountered.
         */
        void loginTaskIncomplete(String message);
    }
}
