package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONTree;
import marabillas.loremar.taskador.json.JSONTreeException;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;

/**
 * RunnableTask that sends POST request for adding new tasks to account. Call run to execute this
 * task.
 */
public class AddTasksTask extends ReauthenticatingTask<AddTasksTask.ResultHandler> {
    private String username;
    private String token;
    private String[] tasks;

    /**
     * @param username account's username
     * @param token    auth token
     * @param tasks    an array of new tasks to be added to account
     */
    public AddTasksTask(String username, String token, String[] tasks) {
        super(username);
        this.username = username;
        this.token = token;
        this.tasks = tasks;

        setRequestUrl(BuildConfig.backend_url + "tasks/add-tasks.php");
    }

    @Override
    public void run() {
        try {
            // Convert fields to JSON to be sent for request
            String json = new JSONTree()
                    .put("username", username)
                    .put("token", token)
                    .put("tasks", tasks)
                    .toString();

            // Send request
            postJSON(json);
        } catch (JSONTreeException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.addTasksTaskFailedToPrepareJSONData(e.getMessage());
            }
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedAddTasksRequest(e.getMessage());
            }
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.newTasksSavedSuccessfully(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToAddTasks(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backendUnableToAddTasks(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.addTasksTaskIncomplete(message);
        }
    }

    @Override
    public void onReauthenticationComplete(String newToken) {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        tasker.addTasks(getResultHandler(), username, newToken, tasks);
    }

    /**
     * Callback interface after validating and handling back-end's response.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback when new tasks are successfully added to account
         */
        void newTasksSavedSuccessfully(String message);

        /**
         * Callback when unable to construct JSON data
         */
        void addTasksTaskFailedToPrepareJSONData(String message);

        /**
         * Callback method when IOException occur while sending POST request to the back-end server.
         */
        void failedAddTasksRequest(String message);

        /**
         * Callback method when back-end server can't add tasks due to either client error or server
         * error.
         */
        void backendUnableToAddTasks(String message);

        /**
         * Callback method when InterruptedException or ExecutionException is encountered.
         */
        void addTasksTaskIncomplete(String message);
    }
}
