package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.FinishTasksTask;

public class FinishTasksTest extends ResultHandlerTest implements FinishTasksTask
        .ResultHandler {
    @Override
    public void finishedTasksSavedSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void finishTasksTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedFinishTasksRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToFinishTask(String message) {
        handleFailure(message);
    }

    @Override
    public void finishTasksTaskIncomplete(String message) {
        handleFailure(message);
    }
}
