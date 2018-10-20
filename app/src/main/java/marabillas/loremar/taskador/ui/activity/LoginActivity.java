package marabillas.loremar.taskador.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.LoginBackgroundTasker;

/**
 * This activity facilitates the Login screen. The screen is shown to the user when taskador can
 * not authenticate the user. The user have to log in with his/her username and password. The
 * server then returns an auth token which will be used for the user's account authentication.
 * The screen also provides sign up option if he/she doesn't have an account yet.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private LoginBackgroundTasker loginBackgroundTasker;
    private EditText usernameBox;
    private EditText passwordBox;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameBox = findViewById(R.id.activity_login_username_box);
        passwordBox = findViewById(R.id.activity_login_password_box);
        loginButton = findViewById(R.id.activity_login_login_button);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {
        loginBackgroundTasker = (LoginBackgroundTasker) backgroundTasker;
        loginBackgroundTasker.bindClient(this);
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {

    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String username = String.valueOf(usernameBox.getText());
            String password = String.valueOf(passwordBox.getText());

            loginBackgroundTasker.login(username, password);
        }
    }
}
