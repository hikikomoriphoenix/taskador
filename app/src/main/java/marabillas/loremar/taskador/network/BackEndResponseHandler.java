package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.FailedToParseException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONParser;
import marabillas.loremar.taskador.network.tasks.ResultListener;

public class BackEndResponseHandler {
    private CookieHandledTracker cookieHandledTracker;

    public BackEndResponseHandler(CookieHandledTracker cookieHandledTracker) {
        this.cookieHandledTracker = cookieHandledTracker;
    }

    public void handle(BackEndResponse response, ResultListener resultListener, boolean
            responseIsValid) {
        if (responseIsValid) {
            int statusCode = response.getStatusCode();
            String data = response.getData();
            String message;
            switch (statusCode) {
                case 200:
                    JSON json = null;
                    try {
                        json = new JSONParser().parse(data);
                    } catch (FailedToParseException ignore) {
                    }
                    resultListener.onStatusOK("Back-end process success.", json);
                    break;
                case 400:
                case 422:
                    message = getMessage(data);
                    if (message == null) {
                        message = "Client Error!";
                    }
                    resultListener.onClientError(message);
                    break;
                case 500:
                    message = getMessage(data);
                    if (message == null) {
                        message = "Server Error!";
                    }
                    resultListener.onServerError(message);
                    break;
            }
        } else {
            String details;
            if (response == null) {
                details = "Response = null";
            } else {
                String contentType = response.getContentType();
                details = "Content-Type = " + contentType;
            }
            resultListener.onServerError("Response is invalid: " + details);
        }
    }

    public boolean validateResponse(BackEndResponse response, String url) throws RecievedACookieException {
        if (response != null) {
            String contentType = response.getContentType();
            if (contentType != null) {
                if (!cookieHandledTracker.isCookieHandled() && contentType.contains("text/html")) {
                    handleSharedHostingCookie(url);
                    throw new RecievedACookieException();
                } else return contentType.equals("application/json");
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void handleSharedHostingCookie(final String url) {
        Context context = App.getInstance().getApplicationContext();
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView(App.getInstance().getApplicationContext());
                WebSettings settings = view.getSettings();
                settings.setJavaScriptEnabled(true);
                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        String cookie = CookieManager.getInstance().getCookie(url);
                        App.getInstance().getSharedPreferences("config", 0)
                                .edit()
                                .putString("shared_hosting_cookie", cookie)
                                .apply();
                        cookieHandledTracker.finalizeCookieHandling();
                    }
                });
                view.loadUrl(url);
            }
        });
    }

    private String getMessage(String data) {
        String message = null;
        try {
            JSON json = new JSONParser().parse(data);
            message = json.getString("message");
        } catch (FailedToParseException ignore) {
        } catch (FailedToGetFieldException ignore) {
        }
        return message;
    }

    static class RecievedACookieException extends Exception {
    }
}
