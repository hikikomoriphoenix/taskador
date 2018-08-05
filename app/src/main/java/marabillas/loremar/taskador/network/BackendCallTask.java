package marabillas.loremar.taskador.network;

import android.os.Handler;
import android.os.HandlerThread;

public class BackendCallTask {
    private HttpClient client;
    private HandlerThread thread;
    private Handler handler;

    BackendCallTask() {
        client = new HttpClient();
        thread = new HandlerThread("BackendCallTask Thread");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public void submitNewAccount(final String username, final String password) {
        handler.post(new SubmitNewAccountTask(client, username, password));
    }
}
