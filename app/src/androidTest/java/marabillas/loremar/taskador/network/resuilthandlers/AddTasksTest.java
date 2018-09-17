package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.AddTasksTask;

public class AddTasksTest extends ResultHandlerTest implements AddTasksTask.ResultHandler {
    @Override
    public void newTasksSavedSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void addTasksTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedAddTasksRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToAddTasks(String message) {
        handleFailure(message);
    }

    @Override
    public void addTasksTaskIncomplete(String message) {
        handleFailure(message);
    }
}
