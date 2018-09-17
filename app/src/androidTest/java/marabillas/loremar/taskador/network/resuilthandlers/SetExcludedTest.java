package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.SetExcludedTask;

public class SetExcludedTest extends ResultHandlerTest implements SetExcludedTask.ResultHandler {
    @Override
    public void selectedWordExcludedSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void setExcludedFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedSetExcludedRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableUnableToExclude(String message) {
        handleFailure(message);
    }

    @Override
    public void setExcludedTaskIncomplete(String message) {
        handleFailure(message);
    }
}
