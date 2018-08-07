package marabillas.loremar.taskador.network;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;

public class SignupTask implements Runnable {
    private String url;
    private BackEndAPICallTasker tasker;
    private String username;
    private String password;
    private BackEndResponse response;

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

    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    public String getSignupUrl() {
        return url;
    }

    public interface Client {
        void newAccountSaved(String message);

        void failedToSubmitNewAccount(String message);

        void backEndUnableToSaveNewAccount(String message);
    }
}
