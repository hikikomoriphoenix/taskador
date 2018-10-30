package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.tasks.AddTaskTask;

public class AddTaskTest extends ResultHandlerTest implements AddTaskTask.ResultHandler {
    @Override
    public void newTaskSavedSuccessfully(String message, String task, JSON data) {
        handleSuccess(message);
    }

    @Override
    public void addTaskTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedAddTaskRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToAddTask(String message) {
        handleFailure(message);
    }

    @Override
    public void addTaskTaskIncomplete(String message) {
        handleFailure(message);
    }
}
