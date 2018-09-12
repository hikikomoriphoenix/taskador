package marabillas.loremar.taskador.network;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;

import marabillas.loremar.taskador.network.tasks.AddTasksTask;
import marabillas.loremar.taskador.network.tasks.LoginTask;
import marabillas.loremar.taskador.network.tasks.RunnableTask;
import marabillas.loremar.taskador.network.tasks.SignupTask;
import marabillas.loremar.taskador.network.tasks.VerifyTokenTask;

/**
 * The BackEndAPICallTasker singleton facilitates communication between taskador and its
 * back-end. Requests are sent using BackEndAPICallTask while responses are handled through
 * BackEndResponseHandler. To get results, a ResultHandler needs to be set for that particular task.
 * No more than one task should be executed at the same time.
 * <p>
 * Example:
 * <pre>{@code
 * public class LoginActivity extends Activity implements LoginTask.ResultHandler {
 *     ...
 *    @literal @Override
 *     public onStart() {
 *         BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
 *         tasker.login(this, "username", "password");
 *     }
 *
 *    @literal @Override
 *     public void loggedInSuccessfuly(String message) {
 *         // handle results
 *     }
 *
 *    @literal @Override
 *     public void failedToSubmitLogin(String message) {
 *         // handle results
 *     }
 *
 *    @literal @Override
 *     public void loginDenied(String message) {
 *         // handle results
 *     }
 *
 *    @literal @Override
 *     public void loginTaskIncomplete(String message) {
 *         // handle results
 *     }
 * }
 * }</pre>
 */
public class BackEndAPICallTasker implements CookieHandledTracker {
    private static BackEndAPICallTasker instance;
    private BackEndAPICallTask task;
    private HttpClient httpClient;
    private boolean cookieHandled;
    private BackEndResponseHandler responseHandler;

    private BackEndAPICallTasker() {
        instance = this;
        httpClient = new HttpClient();
        responseHandler = new BackEndResponseHandler(this);
    }

    @Override
    protected void finalize() {
        if (task != null) {
            task.cancel(true);
        }
    }

    public static BackEndAPICallTasker getInstance() {
        if (instance == null) {
            new BackEndAPICallTasker();
        }
        return instance;
    }

    /**
     * Register a new account. The auth token that comes along with the response will be stored
     * in the device.
     *
     * @param resultHandler callback for handling results
     * @param username username to register with
     * @param password password for this new account
     */
    public void signup(@NonNull SignupTask.ResultHandler resultHandler, final String username, final String
            password) {
        SignupTask signupTask = new SignupTask(username, password);
        signupTask.setResultHandler(resultHandler);
        performTask(signupTask);
    }

    /**
     * Login to account. The auth token that comes along with the response will be stored in the
     * device.
     *
     * @param resultHandler callback for handling results
     * @param username username of account
     * @param password password required to successfully log in
     */
    public void login(@NonNull LoginTask.ResultHandler resultHandler, String username, String
            password) {
        LoginTask loginTask = new LoginTask(username, password);
        loginTask.setResultHandler(resultHandler);
        performTask(loginTask);
    }

    /**
     * Request the back-end server to verify if auth token is correct and authorized to make
     * back-end API calls.
     *
     * @param resultHandler callback for handling results
     * @param username account username
     * @param token auth token to submit for verification
     */
    public void verifyToken(@NonNull VerifyTokenTask.ResultHandler resultHandler, String
            username, String token) {
        VerifyTokenTask verifyTokenTask = new VerifyTokenTask(username, token);
        verifyTokenTask.setResultHandler(resultHandler);
        performTask(verifyTokenTask);
    }

    /**
     * Add new tasks to account.
     *
     * @param resultHandler callback for handling results
     * @param username      account username
     * @param token         auth token
     * @param tasks         array of new tasks to be added
     */
    public void addTasks(@NonNull AddTasksTask.ResultHandler resultHandler, String username,
                         String token, String[] tasks) {
        AddTasksTask addTasksTask = new AddTasksTask(username, token, tasks);
        addTasksTask.setResultHandler(resultHandler);
        performTask(addTasksTask);
    }

    private void performTask(RunnableTask runnableTask) {
        resetCookieHandledTracking();
        if (task != null) {
            task.cancel(true);
        }
        BackEndResponse response = new BackEndResponse();
        task = new BackEndAPICallTask(runnableTask, response);
        task.run();
        obtainAndProcessResult(runnableTask);
    }

    private void obtainAndProcessResult(RunnableTask runnableTask) {
        try {
            BackEndResponse result = task.get();
            String url = runnableTask.getRequestUrl();
            boolean responseIsValid = responseHandler.validateResponse(result, url);
            responseHandler.handle(result, runnableTask, responseIsValid);
        } catch (InterruptedException e) {
            runnableTask.taskIncomplete(e.toString());
        } catch (ExecutionException e) {
            runnableTask.taskIncomplete(e.toString());
        } catch (BackEndResponseHandler.RecievedACookieException ignore) {
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public boolean isCookieHandled() {
        return cookieHandled;
    }

    @Override
    public void resetCookieHandledTracking() {
        cookieHandled = false;
    }

    @Override
    public void finalizeCookieHandling() {
        cookieHandled = true;
        httpClient = new HttpClient();
        task = new BackEndAPICallTask(task.getRunnableTask(), new BackEndResponse());
        task.run();
        obtainAndProcessResult(task.getRunnableTask());
    }
}
