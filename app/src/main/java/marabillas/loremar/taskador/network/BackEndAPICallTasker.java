package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BackEndAPICallTasker {
    private Activity activity;
    private Handler handler;
    private HttpClient client;
    private Runnable currentTask;

    BackEndAPICallTasker(Activity activity) {
        this.activity = activity;

        HandlerThread thread = new HandlerThread("BackEndAPICallTasker Thread");
        thread.start();
        handler = new Handler(thread.getLooper());

        client = new HttpClient(this);
    }

    public void signup(final String username, final String password) {
        SignupTask task = new SignupTask(this, username, password);
        task.setClient((SignupTask.Client) activity);
        handler.post(task);
    }

    public HttpClient getClient() {
        return client;
    }

    public void setCurrentTask(Runnable task) {
        currentTask = task;
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void handleSharedHostingCookie(final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView(activity);
                WebSettings settings = view.getSettings();
                settings.setJavaScriptEnabled(true);
                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        String cookie = CookieManager.getInstance().getCookie(url);
                        activity.getSharedPreferences("config", 0)
                                .edit()
                                .putString("shared_hosting_cookie", cookie)
                                .apply();
                        view.destroy();
                        handler.post(currentTask);
                    }
                });
                view.loadUrl(url);
            }
        });
    }
}
