package marabillas.loremar.taskador.background;

import android.content.Intent;

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
        Intent intent = new Intent(signupActivity, SplashActivity.class);
        intent.putExtra("action", SplashBackgroundTasker.Action.SIGNUP.ordinal());
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        signupActivity.startActivity(intent);
        signupActivity.finish();
        stopSelf();
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
