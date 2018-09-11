package marabillas.loremar.taskador.network;

import android.os.Handler;
import android.os.HandlerThread;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import marabillas.loremar.taskador.BuildConfig;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.network.tasks.ResultListener;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackEndResponseHandlerTest {

    @Test
    public void handle() {
        class CookieHandledTrackerDummy implements CookieHandledTracker {
            @Override
            public boolean isCookieHandled() {
                return false;
            }

            @Override
            public void resetCookieHandledTracking() {

            }

            @Override
            public void finalizeCookieHandling() {

            }
        }

        class ResultsListenerTest implements ResultListener {
            private String message;
            private CountDownLatch pause;

            private ResultsListenerTest() {
                pause = new CountDownLatch(1);
            }

            @Override
            public void onStatusOK(String message, JSON data) {
                this.message = message;
                pause.countDown();
            }

            @Override
            public void onClientError(String message) {
                this.message = message;
                pause.countDown();
            }

            @Override
            public void onServerError(String message) {
                this.message = message;
                pause.countDown();
            }

            private String getMessage() {
                return message;
            }

            private void resetCountDownLatch() {
                pause = new CountDownLatch(1);
            }

            private void waitForResult() {
                try {
                    pause.await();
                } catch (InterruptedException e) {
                    Assert.fail(e.getMessage());
                }
            }
        }

        BackEndResponseHandler backEndResponseHandler = new BackEndResponseHandler(new
                CookieHandledTrackerDummy());
        ResultsListenerTest resultsListenerTest = new ResultsListenerTest();
        BackEndResponse response = new BackEndResponse();
        response.setData("");

        // Test 200
        response.setStatusCode(200);
        backEndResponseHandler.handle(response, resultsListenerTest, true);
        resultsListenerTest.waitForResult();
        assertThat(resultsListenerTest.getMessage(), is("Back-end process success."));

        // Test 400
        response.setStatusCode(400);
        resultsListenerTest.resetCountDownLatch();
        backEndResponseHandler.handle(response, resultsListenerTest, true);
        resultsListenerTest.waitForResult();
        assertThat(resultsListenerTest.getMessage(), is("Client Error!"));

        // Test 422
        response.setStatusCode(422);
        resultsListenerTest.resetCountDownLatch();
        backEndResponseHandler.handle(response, resultsListenerTest, true);
        resultsListenerTest.waitForResult();
        assertThat(resultsListenerTest.getMessage(), is("Client Error!"));

        // Test 500
        response.setStatusCode(500);
        resultsListenerTest.resetCountDownLatch();
        backEndResponseHandler.handle(response, resultsListenerTest, true);
        resultsListenerTest.waitForResult();
        assertThat(resultsListenerTest.getMessage(), is("Server Error!"));

        // Test invalid response
        resultsListenerTest.resetCountDownLatch();
        backEndResponseHandler.handle(null, resultsListenerTest, false);
        resultsListenerTest.waitForResult();
        assertThat(resultsListenerTest.getMessage(), is("Response is invalid"));
    }

    @Test
    public void validateResponse() {
        final String url = BuildConfig.backend_url + "account/signup.php";
        final CountDownLatch pause = new CountDownLatch(1);

        // Track the handling of shared-hosting site's cookie. When handled, try to check if the
        // back end server will return a JSON response.
        CookieHandledTracker tracker = new CookieHandledTracker() {
            private boolean cookieHandled;

            @Override
            public boolean isCookieHandled() {
                return cookieHandled;
            }

            @Override
            public void resetCookieHandledTracking() {
                cookieHandled = false;
            }

            @Override
            public void finalizeCookieHandling() {
                // Sending POST request should be done on a background thread.
                HandlerThread thread = new HandlerThread("network thread");
                thread.start();
                final Handler handler = new Handler(thread.getLooper());
                handler.post(new Runnable() {
                    public void run() {
                        cookieHandled = true;
                        HttpClient client = new HttpClient();
                        HashMap<String, String> form = new HashMap<>();
                        form.put("username", "test1");
                        form.put("password", "password");
                        try {
                            BackEndResponse response = client.postForm(form, url);
                            log("status: " + response.getStatusCode());
                            log("data: " + response.getData());
                            assertThat(response.getContentType(), is("application/json"));
                        } catch (IOException e) {
                            Assert.fail(e.getMessage());
                        } finally {
                            // Unpause the waiting thread, thus, proceeding to the end of this test.
                            pause.countDown();
                            // End this background thread.
                            handler.getLooper().quitSafely();
                        }
                    }
                });
            }
        };

        // Set content-type to 'text/html' before calling validateResponse. This  will trigger
        // the handling of shared-hosting site's cookie.
        BackEndResponseHandler responseHandler = new BackEndResponseHandler(tracker);
        BackEndResponse response = new BackEndResponse();
        response.setContentType("text/html");

        try {
            responseHandler.validateResponse(response, url);
        } catch (BackEndResponseHandler.RecievedACookieException ignore) {
        }

        // This thread needs to wait for further results
        try {
            pause.await();
        } catch (InterruptedException e) {
            Assert.fail("Pause was interrupted");
        }
    }
}