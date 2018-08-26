package marabillas.loremar.taskador.network.resuilthandlers;

import marabillas.loremar.taskador.network.tasks.LoginTask;

public class LoginTest extends ResultHandlerTest implements LoginTask.ResultHandler {
    @Override
    public void loggedInSuccessfuly(String message) {
        handleSuccess(message);
    }

    @Override
    public void failedToSubmitLogin(String message) {
        handleFailure(message);
    }

    @Override
    public void loginDenied(String message) {
        handleFailure(message);
    }

    @Override
    public void loginTaskIncomplete(String message) {
        handleFailure(message);
    }
}
