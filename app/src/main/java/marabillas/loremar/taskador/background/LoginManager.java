package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.SignupActivity;

public class LoginManager extends BackgroundTaskManager implements LoginBackgroundTasker {
    private LoginActivity loginActivity;

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void switchToSignupScreen() {
        loginActivity.switchScreen(SignupActivity.class, this, null);
    }

    @Override
    public void bindClient(LoginActivity client) {
        loginActivity = client;
    }

    @Override
    public LoginActivity getClient() {
        return loginActivity;
    }
}
