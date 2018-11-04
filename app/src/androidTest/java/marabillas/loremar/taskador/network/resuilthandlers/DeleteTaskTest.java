package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.DeleteTaskTask;

public class DeleteTaskTest extends ResultHandlerTest implements DeleteTaskTask.ResultHandler {
    @Override
    public void taskDeletedSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void deleteTaskTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedDeleteTaskRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToDeleteTask(String message) {
        handleFailure(message);
    }

    @Override
    public void deleteTaskTaskIncomplete(String message) {
        handleFailure(message);
    }
}
