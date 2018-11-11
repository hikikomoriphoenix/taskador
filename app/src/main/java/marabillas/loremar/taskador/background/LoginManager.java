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

package marabillas.loremar.taskador.background;

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
                loginActivity.switchScreen(SplashActivity.class, LoginManager.this, input);
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
