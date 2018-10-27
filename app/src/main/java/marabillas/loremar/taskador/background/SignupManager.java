package marabillas.loremar.taskador.background;

import android.os.Bundle;

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.CheckUsernameAvailabilityTask;
import marabillas.loremar.taskador.ui.activity.SignupActivity;
import marabillas.loremar.taskador.ui.activity.SplashActivity;

import static marabillas.loremar.taskador.utils.LogUtils.logError;

public class SignupManager extends BackgroundTaskManager implements SignupBackgroundTasker, CheckUsernameAvailabilityTask.ResultHandler {
    private SignupActivity signupActivity;

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
        signupActivity.switchScreen(SplashActivity.class, this, input, true);
    }

    @Override
    public void bindClient(SignupActivity client) {
        signupActivity = client;
    }

    @Override
    public SignupActivity getClient() {
        return signupActivity;
    }

    @Override
    public void UsernameAvailabilityCheckResultsObtained(String message, final JSON data) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean available = data.getBoolean("available");
                    if (available) {
                        signupActivity.onUsernameIsAvailable();
                    } else {
                        signupActivity.onUsernameNotAvailable();
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
