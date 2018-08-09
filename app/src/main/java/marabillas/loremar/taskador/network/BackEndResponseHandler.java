package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.FailedToParseException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONParser;

public class BackEndResponseHandler {
    private BackEndAPICallTasker tasker;

    BackEndResponseHandler(BackEndAPICallTasker tasker) {
        this.tasker = tasker;
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
            resultListener.onServerError("Response is invalid");
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
}
