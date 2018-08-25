package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;

public class VerifyTokenTask extends RunnableTask<VerifyTokenTask.ResultHandler> {
    private String username;
    private String token;

    public VerifyTokenTask(String username, String token) {
        this.username = username;
        this.token = token;

        setRequestUrl(BuildConfig.backend_url + "verify-token.php");
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("token", token);

        try {
            postForm(form);
        } catch (IOException e) {
            ResultHandler resultHandler = getResultHandler();
            if (resultHandler != null) {
                resultHandler.failedToRequestVerification(e.getMessage());
            }
        }

    }

    @Override
    public void onStatusOK(String message, JSON data) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.tokenVerifiedCorrect(message);
        }
    }

    @Override
    public void onClientError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.tokenVerifiedNotCorrect(message);
        }
    }

    @Override
    public void onServerError(String message) {
        ResultHandler resultHandler = getResultHandler();
        if (resultHandler != null) {
            resultHandler.BackEndFailedToVerifyToken(message);
        }
    }

    public interface ResultHandler extends RunnableTask.ResultHandler {
        void tokenVerifiedCorrect(String message);

        void failedToRequestVerification(String message);

        void tokenVerifiedNotCorrect(String message);

        void BackEndFailedToVerifyToken(String message);
    }
}
