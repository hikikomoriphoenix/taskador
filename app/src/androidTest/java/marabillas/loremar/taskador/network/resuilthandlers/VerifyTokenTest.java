package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.VerifyTokenTask;

public class VerifyTokenTest extends ResultHandlerTest implements VerifyTokenTask.ResultHandler {
    @Override
    public void tokenVerifiedCorrect(String message) {
        handleSuccess(message);
    }

    @Override
    public void failedToRequestVerification(String message) {
        handleFailure(message);
    }

    @Override
    public void tokenVerifiedNotCorrect(String message) {
        handleFailure(message);
    }

    @Override
    public void BackEndFailedToVerifyToken(String message) {
        handleFailure(message);
    }
}
