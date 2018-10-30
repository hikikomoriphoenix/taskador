package marabillas.loremar.taskador.background;

import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSON_Array;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.AddTaskTask;
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
        MainInAppBackgroundTasker, AddTaskTask.ResultHandler, GetTasksTask.ResultHandler {
    private MainInAppActivity mainInAppActivity;
    private String username;
    private String token;

    private int tasksToAddSubmitSize; // Size of the portion of the list that is currently being
    // submitted.

    private List<IdTaskPair> todoTasks;

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
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().addTask(MainInAppManager.this, username,
                        token, task);
            }
        });
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
    public void newTaskSavedSuccessfully(final String message, final String task, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get the id of the new task to create and add an IdTaskPair entry to the
                    // list. Notify the recycler view's adapter that is bound to this list to
                    // update its view.
                    int id = data.getInt("id");
                    todoTasks.add(new IdTaskPair(id, task));

                    mainInAppActivity.dismissAddTaskProgressDialog();
                    mainInAppActivity.getToDoTasksFragment().notifyTaskAdded(todoTasks.size() - 1);
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    mainInAppActivity.dismissAddTaskProgressDialog();
                    promptError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addTaskTaskFailedToPrepareJSONData(String message) {
        logError(message);
        mainInAppActivity.dismissAddTaskProgressDialog();
        promptError(message);
    }

    @Override
    public void failedAddTaskRequest(String message) {
        logError(message);
        mainInAppActivity.dismissAddTaskProgressDialog();
        promptError(message);
    }

    @Override
    public void backendUnableToAddTask(String message) {
        logError(message);
        mainInAppActivity.dismissAddTaskProgressDialog();
        promptError(message);
    }

    @Override
    public void addTaskTaskIncomplete(String message) {
        logError(message);
        mainInAppActivity.dismissAddTaskProgressDialog();
        promptError(message);
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
                mainInAppActivity.getToDoTasksFragment().bindList(todoTasks);
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

    private void promptError(final String message) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorPopUp(mainInAppActivity, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                });
            }
        });
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
