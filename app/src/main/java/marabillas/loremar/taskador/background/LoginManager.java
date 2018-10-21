package marabillas.loremar.taskador.background;

import android.content.Intent;

import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.SignupActivity;

public class LoginManager extends BackgroundTaskManager implements LoginBackgroundTasker {
    private LoginActivity loginActivity;

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void switchToSignupScreen() {
        Intent intent = new Intent(loginActivity, SignupActivity.class);
        loginActivity.startActivity(intent);
        loginActivity.finish();
        stopSelf();
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
