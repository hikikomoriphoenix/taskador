package marabillas.loremar.taskador.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.FailedToParseException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSONParser;
import marabillas.loremar.taskador.network.tasks.ResultListener;

/**
 * This class is used for evaluating the response before it is passed to another class for
 * presentation purposes.
 */
public class BackEndResponseHandler {
    private CookieHandledTracker cookieHandledTracker;

    /**
     * Creates a {@link BackEndResponseHandler} object.
     *
     * @param cookieHandledTracker a callback tracking the state of the shared-hosting site's cookie
     */
    public BackEndResponseHandler(CookieHandledTracker cookieHandledTracker) {
        this.cookieHandledTracker = cookieHandledTracker;
    }

    /**
     * Evaluates the response and calls the appropriate callback to pass the results of the task.
     *
     * @param response        response to evaluate
     * @param resultListener  callback for results
     * @param responseIsValid true for valid response and false for invalid response.
     */
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
                case 401:
                    resultListener.onUnauthorized();
                    break;
                case 400:
                case 422:
                case 405:
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

    /**
     * Determine if response is valid or not. A valid response is a response which contains JSON
     * data.
     *
     * @param response response to evaluate
     * @param url the request's endpoint which is used in this method to retrieve the
     *            shared-hosting site's cookie as a part of validating the response.
     * @return true if valid, otherwise, false.
     * @throws RecievedACookieException if server returned a test cookie.
     */
    public boolean validateResponse(BackEndResponse response, String url) throws RecievedACookieException {
        if (response != null) {
            String contentType = response.getContentType();
            if (contentType != null) {
                // A host, particularly a shared-hosting site, may redirect a request to an html
                // page that runs a javascript which stores a test cookie. The client would have
                // to send back the cookie proving that the request was indeed coming from a
                // browser and not from a bot. If the host did return a cookie then handle this
                // cookie.
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
                // A webview is used in this process to retrieve the test cookie. After the
                // retrieval, the app would then have to send another request along with the cookie.
                WebView view = new WebView(App.getInstance().getApplicationContext());
                WebSettings settings = view.getSettings();
                settings.setJavaScriptEnabled(true);
                view.setWebViewClient(new WebViewClient() {
                    private boolean redirected;

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        redirected = true;
                        return super.shouldOverrideUrlLoading(view, request);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // In some cases, the onPageFinished is called more than once. Once
                        // before redirection and another after redirection. The redirected field
                        // tracks this redirection and ensures that the code below is only
                        // executed once.
                        if (!redirected) {
                            String cookie = CookieManager.getInstance().getCookie(url);
                            App.getInstance().getSharedPreferences("config", 0)
                                    .edit()
                                    .putString("shared_hosting_cookie", cookie)
                                    .apply();
                            cookieHandledTracker.finalizeCookieHandling();
                        }
                        redirected = true;
                    }
                });
                view.loadUrl(url);
            }
        });
    }

    /**
     * Parses the JSON string to get the message field's value.
     *
     * @param data JSON string
     * @return message field value
     */
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

    /**
     * An exception to interrupt the validateResponse method upon receiving a shared-hosting
     * cookie. This exception is meant to be ignored.
     */
    static class RecievedACookieException extends Exception {
    }
}
