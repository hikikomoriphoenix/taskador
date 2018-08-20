package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.BackEndResponse;

public class SignupTask extends RunnableTask<SignupTask.ResultHandler> {
    private BackEndAPICallTasker tasker;
    private String username;
    private String password;

    public SignupTask(BackEndAPICallTasker tasker, String username, String password) {
        this.tasker = tasker;
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
            BackEndResponse backEndResponse = tasker.getHttpClient().postForm(form, getRequestUrl());
            getResponse().setStatusCode(backEndResponse.getStatusCode());
            getResponse().setContentType(backEndResponse.getContentType());
            getResponse().setData(backEndResponse.getData());
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedToSubmitNewAccount(e.getMessage());
            }
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
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

    public interface ResultHandler extends RunnableTask.ResultHandler {
        void newAccountSaved(String message);

        void failedToSubmitNewAccount(String message);

        void backEndUnableToSaveNewAccount(String message);
    }
}
