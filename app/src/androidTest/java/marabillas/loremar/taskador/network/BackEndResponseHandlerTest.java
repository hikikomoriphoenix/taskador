package marabillas.loremar.taskador.network;

import android.os.Handler;
import android.os.HandlerThread;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import marabillas.loremar.taskador.BuildConfig;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BackEndResponseHandlerTest {

    @Test
    public void handle() {
    }

    @Test
    public void validateResponse() {
        final String url = BuildConfig.backend_url + "signup.php";
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
                            assertThat(response.getContentType(), is("application/json"));
                            log("status: " + response.getStatusCode());
                            log("data: " + response.getData());
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

        boolean valid = responseHandler.validateResponse(response, url);

        assertThat(valid, is(false));

        // This thread needs to wait for further results
        try {
            pause.await();
        } catch (InterruptedException e) {
            Assert.fail("Pause was interrupted");
        }
    }
}