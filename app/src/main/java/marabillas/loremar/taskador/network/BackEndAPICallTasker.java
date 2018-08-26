package marabillas.loremar.taskador.network;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;

import marabillas.loremar.taskador.network.tasks.LoginTask;
import marabillas.loremar.taskador.network.tasks.RunnableTask;
import marabillas.loremar.taskador.network.tasks.SignupTask;
import marabillas.loremar.taskador.network.tasks.VerifyTokenTask;

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

    public void signup(@NonNull SignupTask.ResultHandler resultHandler, final String username, final String
            password) {
        SignupTask signupTask = new SignupTask(username, password);
        signupTask.setResultHandler(resultHandler);
        performTask(signupTask);
    }

    public void login(@NonNull LoginTask.ResultHandler resultHandler, String username, String
            password) {
        LoginTask loginTask = new LoginTask(username, password);
        loginTask.setResultHandler(resultHandler);
        performTask(loginTask);
    }

    public void verifyToken(@NonNull VerifyTokenTask.ResultHandler resultHandler, String
            username, String token) {
        VerifyTokenTask verifyTokenTask = new VerifyTokenTask(username, token);
        verifyTokenTask.setResultHandler(resultHandler);
        performTask(verifyTokenTask);
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
