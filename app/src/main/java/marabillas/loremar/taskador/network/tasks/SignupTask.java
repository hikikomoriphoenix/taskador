package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;

import static marabillas.loremar.taskador.utils.AccountUtils.createAccount;
import static marabillas.loremar.taskador.utils.LogUtils.logError;

public class SignupTask extends RunnableTask<SignupTask.ResultHandler> {
    private String username;
    private String password;

    public SignupTask(String username, String password) {
        this.username = username;
        this.password = password;

        setRequestUrl(BuildConfig.backend_url + "signup.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);

        try {
            postForm(form);
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedToSubmitNewAccount(e.getMessage());
            }
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        try {
            String token = data.getString("token");
            createAccount(App.getInstance().getApplicationContext(), username, password, token);
        } catch (FailedToGetFieldException e) {
            logError("Couldn't add account to Account Manager. No available auth token to set: ");
        }

        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.newAccountSaved(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void taskIncomplete(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.signupTaskIncomplete(message);
        }
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        void newAccountSaved(String message);

        void failedToSubmitNewAccount(String message);

        void backEndUnableToSaveNewAccount(String message);

        void signupTaskIncomplete(String message);
    }
}
