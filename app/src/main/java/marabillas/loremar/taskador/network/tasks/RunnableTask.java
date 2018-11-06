package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.BackEndResponse;
import marabillas.loremar.taskador.network.HttpClient;

import static marabillas.loremar.taskador.utils.NetworkUtils.checkNetworkConnection;

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
        try {
            BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
            HttpClient httpClient = tasker.getHttpClient();
            BackEndResponse backEndResponse = httpClient.postForm(form, getRequestUrl());
            saveResult(backEndResponse);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
     * Execute a POST request with JSON data
     *
     * @param json string representing a JSON data
     */
    void postJSON(String json) {
        try {
            BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
            HttpClient httpClient = tasker.getHttpClient();
            BackEndResponse backEndResponse = httpClient.postJSON(json, getRequestUrl());
            saveResult(backEndResponse);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void handleIOException(IOException e) {
        if (e.getMessage().equals("thread interrupted")) {
            BackEndAPICallTasker.getInstance().cancelTask();
            return;
        }

        // Check if IOException is due to lack of network connection.
        boolean connected = checkNetworkConnection();

        if (!connected) {
            failedRequest(ResultHandler.NO_INTERNET_CONNECTION);
        } else {
            failedRequest(e.getMessage());
        }

        BackEndAPICallTasker.getInstance().cancelTask();
    }

    /**
     * A callback interface to handle this task's results.
     */
    interface ResultHandler {
        String NO_INTERNET_CONNECTION = "No Internet Connection.";
    }
}
