package marabillas.loremar.taskador.network;

import java.util.HashMap;

public class SignupTask implements Runnable {
    private final String url = "";
    private BackEndAPICallTasker tasker;
    private String username;
    private String password;
    private Client client;

    SignupTask(BackEndAPICallTasker tasker, String username, String password) {
        this.tasker = tasker;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);
        BackEndResponse response = tasker.getClient().postForm(form, url);
        if (tasker.validateResponse(response, url)) {
            // TODO handle response
        } else {
            // TODO handle invalid response
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public interface Client {
        void newAccountSaved();

        void failedToSubmitNewAccount();

        void backEndUnableToSaveNewAccount();
    }
}
