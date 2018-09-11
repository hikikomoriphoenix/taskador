package marabillas.loremar.taskador.network.tasks;

import marabillas.loremar.taskador.json.JSON;

/**
 * RunnableTask that sends POST request for adding new tasks to account. Call run to execute this
 * task.
 */
public class AddTasksTask extends RunnableTask<AddTasksTask.ResultHandler> {
    private String username;
    private String token;
    private String[] tasks;

    /**
     * @param username account's username
     * @param token    auth token
     * @param tasks    an array of new tasks to be added to account
     */
    public AddTasksTask(String username, String token, String[] tasks) {
        this.username = username;
        this.token = token;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        // TODO Convert fields to JSON to be sent for request

        // TODO Send request
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

    /**
     * Callback interface after validating and handling back-end's response.
     */
    public interface ResultHandler extends RunnableTask.ResultHandler {
        /**
         * Callback when new tasks are successfully added to account
         */
        void newTasksSavedSuccessfully(String message);

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
