package marabillas.loremar.taskador.network;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BackEndAPICallTasker {
    private static BackEndAPICallTasker instance;
    private WeakReference<Activity> activity;
    private FutureTask<?> task;
    private HttpClient httpClient;
    private boolean receivedCookie;
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

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }

    public FutureTask<?> getTask() {
        return task;
    }

    public void signup(final String username, final String password) {
        receivedCookie = false;
        SignupTask signupTask = new SignupTask(this, username, password);
        if (task != null) {
            task.cancel(true);
        }
        BackEndResponse response = new BackEndResponse();
        task = new FutureTask<>(signupTask, response);
        signupTask.trackForResult(response);
        task.run();
        BackEndResponse result;
        try {
            result = (BackEndResponse) task.get();
            boolean responseIsValid = responseHandler.validateResponse(result, signupTask
                    .getSignupUrl());
            responseHandler.handleSignupTaskResponse(result, (SignupTask.Client) activity.get(),
                    responseIsValid);
        } catch (InterruptedException e) {
            responseHandler.handleSignupTaskResponse(null, (SignupTask.Client) activity.get(),
                    false);
        } catch (ExecutionException e) {
            responseHandler.handleSignupTaskResponse(null, (SignupTask.Client) activity.get(),
                    false);
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void handleRequestFailure(Runnable task, String message) {
        if (task instanceof SignupTask) {
            ((SignupTask.Client) activity.get()).failedToSubmitNewAccount(message);
        }
    }

    public boolean receivedCookie() {
        return receivedCookie;
    }

    public void setReceivedCookie(boolean receivedCookie) {
        this.receivedCookie = receivedCookie;
    }
}
