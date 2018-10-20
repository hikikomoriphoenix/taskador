package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.LoginActivity;

public class LoginManager extends BackgroundTaskManager implements LoginBackgroundTasker {
    @Override
    public void login(String username, String password) {

    }

    @Override
    public void switchToSignupScreen() {

    }

    @Override
    public void bindClient(LoginActivity client) {

    }

    @Override
    public LoginActivity getClient() {
        return null;
    }
}
