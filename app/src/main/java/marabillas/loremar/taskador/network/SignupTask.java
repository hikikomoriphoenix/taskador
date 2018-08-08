package marabillas.loremar.taskador.network;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;

public class SignupTask extends RunnableTask {
    private BackEndAPICallTasker tasker;
    private String username;
    private String password;
    private ResultHandler resultHandler;

    SignupTask(BackEndAPICallTasker tasker, String username, String password) {
        this.tasker = tasker;
        this.username = username;
        this.password = password;

        url = BuildConfig.backend_url + "signup.php";
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);

        try {
            response = tasker.getHttpClient().postForm(form, url);
        } catch (IOException e) {
            tasker.handleRequestFailure(this, e.getMessage());
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        resultHandler.newAccountSaved(message);
    }

    @Override
    public void onClientError(String message) {
        resultHandler.backEndUnableToSaveNewAccount(message);
    }

    @Override
    public void onServerError(String message) {
        resultHandler.backEndUnableToSaveNewAccount(message);
    }

    @Override
    public void setResultHandler(RunnableTask.ResultHandler resultHandler) {
        this.resultHandler = (ResultHandler) resultHandler;
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        void newAccountSaved(String message);

        void failedToSubmitNewAccount(String message);

        void backEndUnableToSaveNewAccount(String message);
    }
}
