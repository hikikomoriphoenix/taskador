package marabillas.loremar.taskador.background;

import android.content.DialogInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.entries.WordCountPair;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSON_Array;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.AddTaskTask;
import marabillas.loremar.taskador.network.tasks.DeleteTaskTask;
import marabillas.loremar.taskador.network.tasks.FinishTasksTask;
import marabillas.loremar.taskador.network.tasks.GetExcludedWordsTask;
import marabillas.loremar.taskador.network.tasks.GetFinishedTasksTask;
import marabillas.loremar.taskador.network.tasks.GetTasksTask;
import marabillas.loremar.taskador.network.tasks.GetTopWordsTask;
import marabillas.loremar.taskador.network.tasks.SetExcludedTask;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;
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
        MainInAppBackgroundTasker, AddTaskTask.ResultHandler, GetTasksTask.ResultHandler,
        FinishTasksTask.ResultHandler, DeleteTaskTask.ResultHandler, GetFinishedTasksTask
        .ResultHandler, GetTopWordsTask.ResultHandler, GetExcludedWordsTask.ResultHandler, SetExcludedTask.ResultHandler {
    private MainInAppActivity mainInAppActivity;
    private String username;
    private String token;

    private List<IdTaskPair> todoTasks;
    private List<IdTaskPair> tasksToFinish;
    private List<TaskDatePair> finishedTasks;
    private List<WordCountPair> topWords;
    private List<String> excludedWords;

    private int todoTaskDeletedPosition;
    private boolean submittingFinishedTasks;
    private int sizeOfFinishedTasksSubmitted;
    private boolean isFetchingData;
    private int setExcludedWordPosition;

    private Runnable currentFetchTask;

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
        tasksToFinish = new ArrayList<>();
        finishedTasks = new ArrayList<>();
        topWords = new ArrayList<>();
        excludedWords = new ArrayList<>();

        todoTaskDeletedPosition = -1;
    }

    @Override
    public MainInAppActivity getClient() {
        return mainInAppActivity;
    }

    @Override
    public void fetchToDoTasksList() {
        fetch(new Runnable() {
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
    public void deleteToDoTask(final int position) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                // Note the task to delete in the list when back-end task is successful
                todoTaskDeletedPosition = position;

                // Send request to delete task.
                IdTaskPair task = todoTasks.get(position);
                BackEndAPICallTasker.getInstance().deleteTask(MainInAppManager.this, username,
                        token, task.id);
            }
        });
    }

    @Override
    public void submitFinishedTask(final int position) {
        // Given the position of the task in the list to be set finished, get the task and its id.
        // Allow to save the new finished task while BackEndAPICallTasker is currently submitting
        // previously set finished tasks. When the current submission is finished, the newly
        // saved finished tasks that has not been submitted yet will be submitted next. This is a
        // strategy to minimizes POST requests.
        String taskToFinish = todoTasks.get(position).task;
        int idOfTaskToFinish = todoTasks.get(position).id;
        tasksToFinish.add(new IdTaskPair(idOfTaskToFinish, taskToFinish));

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!submittingFinishedTasks) {
                    submittingFinishedTasks = true;
                    submitFinishedTasksEntries();
                }
            }
        });
    }

    private void submitFinishedTasksEntries() {
        // Take note of the number of tasks being submitted. This value will help determine the
        // range of tasks to remove in the list, removing all successfully submitted tasks.
        sizeOfFinishedTasksSubmitted = tasksToFinish.size();
        IdTaskPair[] idTaskEntries = tasksToFinish.toArray(new
                IdTaskPair[sizeOfFinishedTasksSubmitted]);
        // Begin back-end API call
        BackEndAPICallTasker.getInstance().finishTasks(MainInAppManager.this,
                username, token, idTaskEntries);
    }

    @Override
    public void fetchFinishedTasksList() {
        fetch(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().getFinishedTasks(MainInAppManager.this,
                        username, token);
            }
        });
    }

    @Override
    public void fetchTopWordsList(final int numResults) {
        fetch(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().getTopWords(MainInAppManager.this, username,
                        token, numResults);
            }
        });
    }

    @Override
    public void fetchExcludedWordsList() {
        fetch(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().getExcludedWords(MainInAppManager.this,
                        username, token);
            }
        });
    }

    private void fetch(Runnable task) {
        // Make sure list items are swipable after this fetch.
        mainInAppActivity.onListItemSelectionClear();

        getHandler().removeCallbacks(currentFetchTask);
        currentFetchTask = task;
        if (isFetchingData) {
            BackEndAPICallTasker.getInstance().cancelTask();
        }
        isFetchingData = true;
        getHandler().post(currentFetchTask);
    }

    @Override
    public void setExcluded(final int selectedItemPosition, final int excluded) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                setExcludedWordPosition = selectedItemPosition;
                switch (excluded) {
                    case 1:
                        String topWord = topWords.get(selectedItemPosition).word;
                        BackEndAPICallTasker.getInstance().setExcluded(MainInAppManager.this,
                                username, token, topWord, excluded);
                        break;
                    case 0:
                        String excludedWord = excludedWords.get(selectedItemPosition);
                        BackEndAPICallTasker.getInstance().setExcluded(MainInAppManager.this,
                                username, token, excludedWord, excluded);
                        break;
                }
            }
        });
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

                    mainInAppActivity.dismissProgressDialog();
                    mainInAppActivity.getToDoTasksFragment().notifyTaskAdded(todoTasks.size() - 1);
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    mainInAppActivity.dismissProgressDialog();
                    promptError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addTaskTaskFailedToPrepareJSONData(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void failedAddTaskRequest(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void backendUnableToAddTask(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void addTaskTaskIncomplete(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void tasksObtained(final String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                isFetchingData = false;
                currentFetchTask = null;
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
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToGiveTasks(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void getTasksTaskIncomplete(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void finishedTasksSavedSuccessfully(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                // Remove successfully submitted finished tasks from the list.
                tasksToFinish.subList(0, sizeOfFinishedTasksSubmitted).clear();

                // Submit remaining finished tasks. If none, set the submittingFinishedTasks flag
                // to false indicating that BackEndAPICallTasker is not currently submitting
                // finished tasks. This flag if set to false allows initiating future finished
                // tasks submission.
                if (tasksToFinish.size() > 0) {
                    submitFinishedTasksEntries();
                } else {
                    submittingFinishedTasks = false;
                }
            }
        });
    }

    @Override
    public void finishTasksTaskFailedToPrepareJSONData(String message) {
        submittingFinishedTasks = false;
        logError(message);
    }

    @Override
    public void failedFinishTasksRequest(String message) {
        submittingFinishedTasks = false;
        logError(message);
    }

    @Override
    public void backendUnableToFinishTask(String message) {
        submittingFinishedTasks = false;
        logError(message);
    }

    @Override
    public void finishTasksTaskIncomplete(String message) {
        submittingFinishedTasks = false;
        logError(message);
    }

    @Override
    public void taskDeletedSuccessfully(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mainInAppActivity.dismissProgressDialog();
                mainInAppActivity.getToDoTasksFragment().removeTask(todoTaskDeletedPosition);
                todoTaskDeletedPosition = -1;
            }
        });
    }

    @Override
    public void deleteTaskTaskFailedToPrepareJSONData(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void failedDeleteTaskRequest(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void backendUnableToDeleteTask(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void deleteTaskTaskIncomplete(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void finishedTasksObtained(final String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                isFetchingData = false;
                currentFetchTask = null;
                finishedTasks.clear();

                try {
                    JSON_Array tasks = data.getArray("tasks");

                    SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd " +
                            "HH:ms:ss", Locale.getDefault());
                    SimpleDateFormat targetDateFormat = new SimpleDateFormat("EEE, MMM dd", Locale
                            .getDefault());

                    for (int i = 0; i < tasks.getCount(); ++i) {
                        JSON taskObject = tasks.getObject(i);
                        String task = taskObject.getString("task");
                        String dateFinished = taskObject.getString("date_finished");

                        // Format date finished to desired format.
                        Date date = sourceDateFormat.parse(dateFinished);
                        dateFinished = targetDateFormat.format(date);

                        TaskDatePair taskDatePair = new TaskDatePair(task, dateFinished);
                        finishedTasks.add(taskDatePair);
                    }
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    promptErrorAndLogout(e.getMessage());
                } catch (ParseException e) {
                    logError(e.getMessage());
                    promptErrorAndLogout(e.getMessage());
                }

                mainInAppActivity.getFinishedTasksFragment().showRecyclerView();
                mainInAppActivity.getFinishedTasksFragment().bindList(finishedTasks);
            }
        });
    }

    @Override
    public void failedGetFinishedTasksRequest(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToGiveFinishedTasks(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void getFinishedTasksIncomplete(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void topWordsObtained(String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                isFetchingData = false;
                currentFetchTask = null;
                topWords.clear();

                try {
                    JSON_Array words = data.getArray("top_words");
                    for (int i = 0; i < words.getCount(); ++i) {
                        JSON wordObject = words.getObject(i);
                        String word = wordObject.getString("word");
                        String count = wordObject.getString("count");
                        WordCountPair wordCountPair = new WordCountPair(word, count);
                        topWords.add(wordCountPair);
                    }
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    promptErrorAndLogout(e.getMessage());
                }

                mainInAppActivity.getTopWordsFragment().showRecyclerView();
                mainInAppActivity.getTopWordsFragment().bindTopWordsList(topWords);
            }
        });
    }

    @Override
    public void getTopWordsTaskFailedToPrepareJSONData(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void failedGetTopWordsRequest(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToGetTopWords(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void getTopWordsTaskIncomplete(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void excludedWordsObtained(String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                isFetchingData = false;
                currentFetchTask = null;
                excludedWords.clear();

                try {
                    JSON_Array words = data.getArray("words");
                    for (int i = 0; i < words.getCount(); ++i) {
                        String word = words.getString(i);
                        excludedWords.add(word);
                    }
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                    promptErrorAndLogout(e.getMessage());
                }

                mainInAppActivity.getTopWordsFragment().showRecyclerView();
                mainInAppActivity.getTopWordsFragment().bindExcludedWordsList(excludedWords);
            }
        });
    }

    @Override
    public void failedGetExcludedRequest(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void backendUnableToGiveExcludedWords(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void getExcludedWordsTaskIncomplete(String message) {
        isFetchingData = false;
        logError(message);
        promptErrorAndLogout(message);
    }

    @Override
    public void selectedWordSetExcludedSuccessfully(String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mainInAppActivity.dismissProgressDialog();
                TopWordsFragment.ViewState viewState = mainInAppActivity.getTopWordsFragment()
                        .getCurrentViewState();
                switch (viewState) {
                    case TOP:
                        mainInAppActivity.getTopWordsFragment().removeTopWord
                                (setExcludedWordPosition);
                        break;
                    case EXCLUDED:
                        mainInAppActivity.getTopWordsFragment().removeExcludedWord
                                (setExcludedWordPosition);
                        break;
                }

                setExcludedWordPosition = -1;
            }
        });
    }

    @Override
    public void setExcludedFailedToPrepareJSONData(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void failedSetExcludedRequest(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void backendUnableUnableToExclude(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
    }

    @Override
    public void setExcludedTaskIncomplete(String message) {
        logError(message);
        mainInAppActivity.dismissProgressDialog();
        promptError(message);
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
