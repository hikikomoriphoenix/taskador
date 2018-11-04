package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} for deleting a to-do task in account.
 */
public class DeleteTaskTask extends ReauthenticatingTask<DeleteTaskTask.ResultHandler> {
    private String username;
    private String token;
    private int id;

    /**
     * @param username account username
     * @param token    auth token
     * @param id       id of the task to delete
     */
    public DeleteTaskTask(String username, String token, int id) {
        super(username);
        this.username = username;
        this.token = token;
        this.id = id;

        setRequestUrl(BuildConfig.backend_url + "tasks/delete-task.php");
    }

    @Override
    public void run() {
        try {
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("id", id)
                    .toString();

            postJSON(json);
        } catch (JSONTreeException e) {
            DeleteTaskTask.ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.deleteTaskTaskFailedToPrepareJSONData(e.getMessage());
            }
            BackEndAPICallTasker.getInstance().cancelTask();
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.taskDeletedSuccessfully(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToDeleteTask(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToDeleteTask(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedDeleteTaskRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.deleteTaskTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.deleteTask(getResultHandler(), username, newToken, id);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback when a to-do task is successfully deleted
         */
        void taskDeletedSuccessfully(String message);

        /**
         * Callback when unable to construct JSON data
         */
        void deleteTaskTaskFailedToPrepareJSONData(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedDeleteTaskRequest(String message);

        /**
         * Callback method when back-end is unable to delete a to-do task due to client or server
         * error.
         */
        void backendUnableToDeleteTask(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void deleteTaskTaskIncomplete(String message);
    }
}
