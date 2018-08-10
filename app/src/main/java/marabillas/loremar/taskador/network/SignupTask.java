package marabillas.loremar.taskador.network;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;

public class SignupTask extends RunnableTask {
    private BackEndAPICallTasker tasker;
    private String username;
    private String password;
    private WeakReference<ResultHandler> resultHandlerReference;

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
            ResultHandler resultHandler = resultHandlerReference.get();
            if (resultHandler != null) {
                resultHandler.failedToSubmitNewAccount(e.getMessage());
            }
        }
    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = resultHandlerReference.get();
        if (resultHandler != null) {
            resultHandler.newAccountSaved(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = resultHandlerReference.get();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = resultHandlerReference.get();
        if (resultHandler != null) {
            resultHandler.backEndUnableToSaveNewAccount(message);
        }
    }

    @Override
    public void setResultHandler(RunnableTask.ResultHandler resultHandler) {
        this.resultHandlerReference = new WeakReference<>((ResultHandler) resultHandler);
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        void newAccountSaved(String message);

        void failedToSubmitNewAccount(String message);

        void backEndUnableToSaveNewAccount(String message);
    }
}
