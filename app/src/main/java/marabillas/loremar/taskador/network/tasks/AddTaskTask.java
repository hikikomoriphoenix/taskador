package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * {@link RunnableTask} that sends POST request for adding new to-do task to account.
 */
public class AddTaskTask extends ReauthenticatingTask<AddTaskTask.ResultHandler> {
    private String username;
    private String token;
    private String task;

    /**
     * @param username account's username
     * @param token    auth token
     * @param task     a new to-do task to add to account
     */
    public AddTaskTask(String username, String token, String task) {
        super(username);
        this.username = username;
        this.token = token;
        this.task = task;

        setRequestUrl(BuildConfig.backend_url + "tasks/add-task.php");
    }

    @Override
    public void run() {
        try {
            // Convert fields to JSON to be sent for request
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("task", task)
                    .toString();

            // Send request
            postJSON(json);
        } catch (JSONTreeException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.addTaskTaskFailedToPrepareJSONData(e.getMessage());
            }
            BackEndAPICallTasker.getInstance().cancelTask();
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.newTaskSavedSuccessfully(message, task, data);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToAddTask(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToAddTask(message);
        }
    }

    @Override
    public void failedRequest(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.failedAddTaskRequest(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.addTaskTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.addTask(getResultHandler(), username, newToken, task);
    }

    /**
     * Callback interface after validating and handling back-end's response.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback when new task is successfully added to account
         *
         * @param task the new task that was just added
         * @param data JSON data containing the id of the new task added.<br/>
         *             Structure:
         *             {@code
         *             {
         *             "id":'id of new task'
         *             }
         *             }
         */
        void newTaskSavedSuccessfully(String message, String task, JSON data);

        /**
         * Callback when unable to construct JSON data
         */
        void addTaskTaskFailedToPrepareJSONData(String message);

        /**
         * Callback method when {@link IOException} occur while sending POST request to the back-end server.
         */
        void failedAddTaskRequest(String message);

        /**
         * Callback method when back-end server can't add task due to either client error or server
         * error.
         */
        void backendUnableToAddTask(String message);

        /**
         * Callback method when {@link InterruptedException} or
         * {@link java.util.concurrent.ExecutionException} is encountered.
         */
        void addTaskTaskIncomplete(String message);
    }
}
