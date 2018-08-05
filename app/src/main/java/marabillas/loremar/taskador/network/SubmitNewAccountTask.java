package marabillas.loremar.taskador.network;

import java.util.HashMap;

public class SubmitNewAccountTask implements Runnable {
    private final String url = "";
    private HttpClient client;
    private String username;
    private String password;

    SubmitNewAccountTask(HttpClient client, String username, String password) {
        this.client = client;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);
        client.postForm(form, url);
    }
}
