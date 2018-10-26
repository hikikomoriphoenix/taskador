package marabillas.loremar.taskador.background;

import android.content.Intent;
import android.os.Bundle;

import marabillas.loremar.taskador.ui.activity.LoginActivity;
import marabillas.loremar.taskador.ui.activity.SignupActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;

/**
 * Service that handles background tasks for login screen.
 */
public class LoginManager extends BackgroundTaskManager implements LoginBackgroundTasker {
    private LoginActivity loginActivity;

    @Override
    public void login(final String username, final String password) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                // Switch to splash screen and perform login task using the following prepared
                // input.
                Bundle input = new Bundle();
                input.putInt("action", SplashBackgroundTasker.Action.LOGIN.ordinal());
                input.putString("username", username);
                input.putString("password", password);

                Intent intent = new Intent(loginActivity, SplashActivity.class);
                intent.putExtra("input", input);
                loginActivity.startActivity(intent);
            }
        });
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
