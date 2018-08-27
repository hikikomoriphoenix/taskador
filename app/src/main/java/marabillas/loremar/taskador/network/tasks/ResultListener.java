package marabillas.loremar.taskador.network.tasks;

import marabillas.loremar.taskador.json.JSON;

/**
 * A ResultListener is notified when a response has been received from the back-server and
 * validated by a BackEndResponseHandler, by calling one of the callback methods corresponding to
 * the status of the response.
 */
public interface ResultListener {
    /**
     * Callback method for response with 200 OK status.
     */
    void onStatusOK(String message, JSON data);

    /**
     * Callback method for response with 400 or 422 status
     */
    void onClientError(String message);

    /**
     * Callback method for response with 500 INTERNAL SERVER ERROR status
     */
    void onServerError(String message);
}
