package marabillas.loremar.taskador.background;

import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSON_Array;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.AddTasksTask;
import marabillas.loremar.taskador.network.tasks.GetTasksTask;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.utils.AccountUtils;

import static marabillas.loremar.taskador.utils.AccountUtils.getAuthToken;
import static marabillas.loremar.taskador.utils.AccountUtils.getCurrentAccountUsername;
import static marabillas.loremar.taskador.utils.LogUtils.logError;
import static marabillas.loremar.taskador.utils.PopUpUtils.showErrorPopUp;

/**
 * The service used to perform background tasks for the in-app screen. MainInAppActivity has to
 * bind this service to call its methods.
 */
public class MainInAppManager extends BackgroundTaskManager implements
        MainInAppBackgroundTasker, AddTasksTask.ResultHandler, GetTasksTask.ResultHandler {
    private MainInAppActivity mainInAppActivity;
    private String username;
    private String token;

    private List<String> tasksToAdd;
    private int tasksToAddSubmitSize; // Size of the portion of the list that is currently being
    // submitted.

    private List<IdTaskPair> todoTasks;

    private boolean isAddingTask;

    @Override
    public void bindClient(MainInAppActivity client) {
        mainInAppActivity = client;

        // Get current account and its associated token.
        username = getCurrentAccountUsername();
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (username != null) {
                    try {
                        token = getAuthToken(username);
                    } catch (AccountUtils.GetAuthTokenException e) {
                        logError(e.getMessage());
                        promptErrorAndLogout("Unable to get authorization token.");
                    }
                } else {
                    logError("Current account username is null.");
                    promptErrorAndLogout("Unable to get current account username.");
                }
            }
        });

        tasksToAdd = new ArrayList<>();
        todoTasks = new ArrayList<>();
    }

    @Override
    public MainInAppActivity getClient() {
        return mainInAppActivity;
    }

    @Override
    public void fetchToDoTasksList() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().getTasks(MainInAppManager.this, username,
                        token);
            }
        });
    }

    @Override
    public void submitNewTask(final String task) {
        // Queue new task for submission. In the next submission, all the tasks in the queue will
        // be submitted all at once.
        tasksToAdd.add(task);
        // If there is no ongoing submission, initiate submission of new task immediately.
        if (!isAddingTask) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    isAddingTask = true;

                    // This will take note of the range in the list indicating tasks that are
                    // currently being submitted. These will be removed after submission,
                    // excluding aside new tasks that aren't submitted yet.
                    tasksToAddSubmitSize = tasksToAdd.size();

                    // Get the array of new tasks and submit.
                    String[] tasksArray = tasksToAdd.toArray(new String[tasksToAddSubmitSize]);
                    BackEndAPICallTasker.getInstance().addTasks(MainInAppManager.this, username,
                            token, tasksArray);
                }
            });
        }
    }

    @Override
    public void fetchFinishedTasksList() {
        // TODO implement
    }

    @Override
    public void fetchTopWordsList(int numResults) {
        // TODO implement
    }

    @Override
    public void fetchExcludedWordsList() {
        // TODO implement
    }


    @Override
    public void newTasksSavedSuccessfully(String message) {
        // Clear all tasks that was submitted, leaving all the new tasks in the list that are not
        // submitted yet.
        tasksToAdd.subList(0, tasksToAddSubmitSize).clear();

        // If there are new tasks that are yet to be submitted, initiate submission. Otherwise,
        // mark that submission has ended and that there are currently no ongoing submission.
        if (tasksToAdd.size() > 0) {
            tasksToAddSubmitSize = tasksToAdd.size();
            String[] tasksArray = tasksToAdd.toArray(new String[tasksToAddSubmitSize]);
            BackEndAPICallTasker.getInstance().addTasks(MainInAppManager.this, username,
                    token, tasksArray);
        } else {
            isAddingTask = false;
        }
    }

    @Override
    public void addTasksTaskFailedToPrepareJSONData(String message) {
        logError(message);
        isAddingTask = false;
        promptErrorAndLogout(message);
    }

    @Override
    public void failedAddTasksRequest(String message) {
        logError(message);
        isAddingTask = false;
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToAddTasks(String message) {
        logError(message);
        isAddingTask = false;
        promptErrorAndLogout(message);
    }

    @Override
    public void addTasksTaskIncomplete(String message) {
        logError(message);
        isAddingTask = false;
        promptErrorAndLogout(message);
    }

    @Override
    public void tasksObtained(final String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                todoTasks.clear();
                // Get all to-do tasks and its corresponding id's from the JSON data and add each
                // pair of these values to the list as an IdTaskPair object. The UI should then be
                // updated with this new updated list.
                try {
                    JSON_Array tasks = data.getArray("tasks");
                    for (int i = 0; i < tasks.getCount(); ++i) {
                        JSON taskObject = tasks.getObject(i);
                        String taskText = taskObject.getString("task");
                        int taskId = taskObject.getInt("id");
                        IdTaskPair taskPair = new IdTaskPair(taskId, taskText);
                        todoTasks.add(taskPair);
                    }
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    promptErrorAndLogout(e.getMessage());
                }
                mainInAppActivity.getToDoTasksFragment().showRecyclerView();
                mainInAppActivity.getToDoTasksFragment().updateList(todoTasks);
            }
        });
    }

    @Override
    public void failedGetTasksRequest(String message) {
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToGiveTasks(String message) {
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void getTasksTaskIncomplete(String message) {
        logError(message);
        promptErrorAndLogout(message);
    }

    private void promptErrorAndLogout(String message) {
        showErrorPopUp(mainInAppActivity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainInAppActivity.logout();
            }
        });
    }
}
