package marabillas.loremar.taskador.network;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;

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
        SignupTask signupTask = new SignupTask(this, username, password);
        performTask(resultHandler, signupTask);
    }

    private void performTask(@NonNull RunnableTask.ResultHandler resultHandler, RunnableTask runnableTask) {
        resetCookieHandledTracking();
        runnableTask.setResultHandler(resultHandler);
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
            responseHandler.handle(null, runnableTask, false);
        } catch (ExecutionException e) {
            responseHandler.handle(null, runnableTask, false);
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
        task.run();
        obtainAndProcessResult(task.getRunnableTask());
    }
}
