package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.SignupTask;

public class SignupTest extends ResultHandlerTest implements SignupTask.ResultHandler {
    @Override
    public void newAccountSaved(String message) {
        handleSuccess(message);
    }

    @Override
    public void failedToSubmitNewAccount(String message) {
        handleFailure(message);
    }

    @Override
    public void backEndUnableToSaveNewAccount(String message) {
        handleFailure(message);
    }

    @Override
    public void signupTaskIncomplete(String message) {
        handleFailure(message);
    }
}
