package marabillas.loremar.taskador.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.LoginBackgroundTasker;
import marabillas.loremar.taskador.background.LoginManager;

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
    private TextView createNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameBox = findViewById(R.id.activity_login_username_box);
        passwordBox = findViewById(R.id.activity_login_password_box);
        loginButton = findViewById(R.id.activity_login_login_button);
        createNewAccount = findViewById(R.id.activity_login_signup_link);

        loginButton.setOnClickListener(this);
        createNewAccount.setOnClickListener(this);
    }

    @Override
    public void onSetupBackgroundService() {
        setupBackgroundService(LoginManager.class);
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {
        loginBackgroundTasker = (LoginBackgroundTasker) backgroundTasker;
        loginBackgroundTasker.bindClient(this);
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        loginBackgroundTasker = (LoginBackgroundTasker) backgroundTaskManager;
        loginBackgroundTasker.bindClient(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String username = String.valueOf(usernameBox.getText());
            String password = String.valueOf(passwordBox.getText());

            // Make sure the user filled all the required fields. Notify if otherwise.
            if (username.isEmpty() || password.isEmpty()) {
                notifyUserToFillInAllFields();
            } else {
                loginBackgroundTasker.login(username, password);
            }
        } else if (v == createNewAccount) {
            loginBackgroundTasker.switchToSignupScreen();
        }
    }

    private void notifyUserToFillInAllFields() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.activity_login_noinputwarning_title)
                        .setMessage(R.string.activity_login_noinputwarning_message)
                        .setPositiveButton(R.string.activity_login_noinputwarning_ok, new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .create()
                        .show();
            }
        });
    }
}
