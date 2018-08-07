package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.FutureTask;

public class BackEndAPICallTasker {
    private Activity activity;
    private FutureTask<?> task;
    private HttpClient httpClient;
    private boolean recievedCookie;

    BackEndAPICallTasker(Activity activity) {
        this.activity = activity;
        httpClient = new HttpClient();
    }

    public void signup(final String username, final String password) {
        SignupTask signupTask = new SignupTask(this, username, password);
        signupTask.setClient((SignupTask.Client) activity);
        if (task != null) {
            task.cancel(true);
        }
        task = new FutureTask<>(signupTask, null);
        task.run();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public boolean validateResponse(BackEndResponse response, String url) {
        if (response != null) {
            String contentType = response.getContentType();
            if (!recievedCookie && contentType.contains("text/html")) {
                handleSharedHostingCookie(url);
                // The task's thread should do nothing while retrieving the cookie
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return false;
            } else return contentType.equals("application/json");
        } else {
            return false;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleSharedHostingCookie(final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Canceling task's thread here allows the execution of the following code.
                // Otherwise, if cancel was called before runOnUiThread then runOnUiThread
                // would have not been called, since runOnUiThread have to be called in the task's
                // thread.
                task.cancel(true);
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
                        recievedCookie = true;
                        view.destroy();
                        task.run();
                    }
                });
                view.loadUrl(url);
            }
        });
    }
}
