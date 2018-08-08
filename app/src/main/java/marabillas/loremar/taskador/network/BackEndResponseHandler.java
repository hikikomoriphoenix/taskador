package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BackEndResponseHandler {
    private BackEndAPICallTasker tasker;

    BackEndResponseHandler(BackEndAPICallTasker tasker) {
        this.tasker = tasker;
    }

    public void handleSignupTaskResponse(BackEndResponse response, SignupTask.ResultHandler resultHandler,
                                         boolean responseIsValid) {
        if (responseIsValid) {
            // TODO handle response
        } else {
            // TODO handle invalid response
        }
    }

    public boolean validateResponse(BackEndResponse response, String url) {
        if (response != null) {
            String contentType = response.getContentType();
            if (tasker.receivedCookie() && contentType.contains("text/html")) {
                handleSharedHostingCookie(url);
                return false;
            } else return contentType.equals("application/json");
        } else {
            return false;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleSharedHostingCookie(final String url) {
        tasker.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView(tasker.getActivity());
                WebSettings settings = view.getSettings();
                settings.setJavaScriptEnabled(true);
                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        String cookie = CookieManager.getInstance().getCookie(url);
                        tasker.getActivity().getSharedPreferences("config", 0)
                                .edit()
                                .putString("shared_hosting_cookie", cookie)
                                .apply();
                        tasker.setReceivedCookie(true);
                        view.destroy();
                        tasker.getTask().run();
                    }
                });
                view.loadUrl(url);
            }
        });
    }
}
