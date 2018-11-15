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

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.CheckUsernameAvailabilityTask;
import marabillas.loremar.taskador.ui.activity.SplashActivity;
import marabillas.loremar.taskador.ui.components.SignupInterface;

import static marabillas.loremar.taskador.utils.LogUtils.logError;

public class SignupManager extends BackgroundTaskManager implements SignupBackgroundTasker, CheckUsernameAvailabilityTask.ResultHandler {
    private SignupInterface signup;

    @Override
    public void checkUsernameAvailability(final String username) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().checkUsernameAvailability(SignupManager.this,
                        username);
            }
        });
    }

    @Override
    public void cancelUsernameAvailabilityCheck() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                BackEndAPICallTasker.getInstance().cancelTask();
            }
        });
    }

    @Override
    public void submitNewAccount(String username, String password) {
        Bundle input = new Bundle();
        input.putInt("action", SplashBackgroundTasker.Action.SIGNUP.ordinal());
        input.putString("username", username);
        input.putString("password", password);
        signup.switchToAnotherScreen(SplashActivity.class, this, input);
    }

    @Override
    public void bindClient(SignupInterface client) {
        signup = client;
    }

    @Override
    public SignupInterface getClient() {
        return signup;
    }

    @Override
    public void UsernameAvailabilityCheckResultsObtained(String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean available = data.getBoolean("available");
                    if (available) {
                        signup.onUsernameIsAvailable();
                    } else {
                        signup.onUsernameNotAvailable();
                    }
                } catch (FailedToGetFieldException e) {
                    logError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void failedUsernameAvailabilityCheckRequest(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }

    @Override
    public void BackendFailedToCheckUsernameAbility(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }

    @Override
    public void CheckUsernameAvailabilityTaskIncomplete(final String message) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                logError(message);
            }
        });
    }
}
