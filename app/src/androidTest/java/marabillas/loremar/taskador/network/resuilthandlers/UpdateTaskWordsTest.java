package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.UpdateTaskWordsTask;

public class UpdateTaskWordsTest extends ResultHandlerTest implements UpdateTaskWordsTask.ResultHandler {
    @Override
    public void wordsUpdateSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void failedUpdateTaskWordsRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToUpdateTaskWords(String message) {
        handleFailure(message);
    }

    @Override
    public void updateTaskWordsTaskIncomplete(String message) {
        handleFailure(message);
    }
}
