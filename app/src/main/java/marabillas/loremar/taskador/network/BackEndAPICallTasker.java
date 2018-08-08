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
        try {
            performTask(signupTask);
        } catch (ClassCastException e) {
            taskError(SignupTask.ResultHandler.class);
        }
    }

    private void performTask(RunnableTask runnableTask) {
        receivedCookie = false;
        runnableTask.setResultHandler((RunnableTask.ResultHandler) activity);
        if (task != null) {
            task.cancel(true);
        }
        BackEndResponse response = new BackEndResponse();
        task = new FutureTask<>(runnableTask, response);
        runnableTask.trackForResult(response);
        task.run();
        BackEndResponse result;
        try {
            result = (BackEndResponse) task.get();
            String url = runnableTask.getRequestUrl();
            boolean responseIsValid = responseHandler.validateResponse(result, url);
            responseHandler.handle(response, runnableTask, responseIsValid);
        } catch (InterruptedException e) {
            responseHandler.handle(null, runnableTask, false);
        } catch (ExecutionException e) {
            responseHandler.handle(null, runnableTask, false);
        }
    }

    private void taskError(Class resultHandlerInterface) {
        throw new ClassCastException("Activity must implement " + resultHandlerInterface.getName()
                + " for this operation");
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void handleRequestFailure(Runnable task, String message) {
        if (task instanceof SignupTask) {
            ((SignupTask.ResultHandler) activity.get()).failedToSubmitNewAccount(message);
        }
    }

    public boolean receivedCookie() {
        return receivedCookie;
    }

    public void setReceivedCookie(boolean receivedCookie) {
        this.receivedCookie = receivedCookie;
    }
}
