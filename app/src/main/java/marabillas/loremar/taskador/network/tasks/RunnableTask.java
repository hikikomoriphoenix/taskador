package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.BackEndResponse;
import marabillas.loremar.taskador.network.HttpClient;

/**
 * Base class for all network-related tasks which involves sending request to the back-end server,
 * and receiving response and passing it to another class for handling.
 *
 * @param <RH> a subclass of {@link ResultHandler} corresponding to the specific RunnableTask subclass
 */
public abstract class RunnableTask<RH extends RunnableTask.ResultHandler> implements Runnable,
        ResultListener {
    private BackEndResponse response;
    private String requestUrl;
    private WeakReference<RH> resultHandlerReference;

    /**
     * Invoked when {@link IOException} is encountered while sending request to back-end.
     */
    public abstract void failedRequest(String message);

    /**
     * Called when {@link InterruptedException} or
     * {@link java.util.concurrent.ExecutionException} is encountered.
     */
    public abstract void taskIncomplete(String message);

    public void setResultHandler(RH resultHandler) {
        resultHandlerReference = new WeakReference<>(resultHandler);
    }

    RH getResultHandler() {
        return resultHandlerReference.get();
    }

    /**
     * Set a reference to a {@link BackEndResponse} object to store the contents of the response once a
     * response has been received from the back-end server.
     *
     * @param response an reference to an empty {@link BackEndResponse} object
     */
    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    /**
     * Store the contents of the response received from the back-end server into a
     * {@link BackEndResponse} object.
     *
     * @param response response sent by the back-end server
     */
    private void saveResult(BackEndResponse response) {
        this.response.setStatusCode(response.getStatusCode());
        this.response.setContentType(response.getContentType());
        this.response.setData(response.getData());
    }

    /**
     * Set the request url. The request url will be the endpoint where the request will be sent
     * to. Each task should have each corresponding endpoint.
     */
    void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    @Override
    public void onUnauthorized() {
        if (this instanceof ReauthenticatingTask) {
            ((ReauthenticatingTask) this).reauthenticate();
        }
    }

    /**
     * Execute a basic POST request
     *
     * @param form a form to send along with the request
     */
    void postForm(Map<String, String> form) {
        BackEndAPICallTasker tasker = null;
        try {
            tasker = BackEndAPICallTasker.getInstance();
            HttpClient httpClient = tasker.getHttpClient();
            BackEndResponse backEndResponse = httpClient.postForm(form, getRequestUrl());
            saveResult(backEndResponse);
        } catch (IOException e) {
            failedRequest(e.getMessage());
            tasker.cancelTask();
        }
    }

    /**
     * Execute a POST request with JSON data
     *
     * @param json string representing a JSON data
     */
    void postJSON(String json) {
        BackEndAPICallTasker tasker = null;
        try {
            tasker = BackEndAPICallTasker.getInstance();
            HttpClient httpClient = tasker.getHttpClient();
            BackEndResponse backEndResponse = httpClient.postJSON(json, getRequestUrl());
            saveResult(backEndResponse);
        } catch (IOException e) {
            failedRequest(e.getMessage());
            tasker.cancelTask();
        }
    }

    /**
     * A callback interface to handle this task's results.
     */
    interface ResultHandler {
    }
}
