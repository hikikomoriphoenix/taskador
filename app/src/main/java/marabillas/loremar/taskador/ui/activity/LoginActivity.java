/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import marabillas.loremar.taskador.account.DirectLogin;
import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.LoginBackgroundTasker;
import marabillas.loremar.taskador.background.LoginManager;

/**
 * Activity facilitating login screen. The login screen is shown when:
 * 1. No account is set to be used for automatic login when launching app. This occurs when user
 * logged out or has not yet logged in once.
 * 2. No auth token available to authenticate account and that {@link DirectLogin} used
 * automatically for logging in using saved password, has also failed.
 * 3. Auth token verification from back-end returns false either because auth token has expired or
 * is incorrect, and that {@link DirectLogin} also failed using saved password.
 * 4. User has just logged out.
 * 5. User chooses to add account to taskador from the device through the settings or Account
 * Manager.
 *
 * You can also click 'Create New Account' at the bottom to go to signup screen to register a new
 * taskador account.
 */
public class LoginActivity extends BaseActivity implements BackgroundServiceClient,
        BackgroundServiceConnection.OnServiceConnectedListener, View
                .OnClickListener {
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
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        setupBackgroundService(serviceClass, this);
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
