package marabillas.loremar.taskador.network;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import marabillas.loremar.taskador.BuildConfig;

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

        CookieHandledTracker tracker = new CookieHandledTracker() {
            private boolean cookieHandle;

            @Override
            public boolean isCookieHandled() {
                return cookieHandle;
            }

            @Override
            public void resetCookieHandledTracking() {
                cookieHandle = false;
            }

            @Override
            public void finalizeCookieHandling() {
                cookieHandle = true;
                HttpClient client = new HttpClient();
                HashMap<String, String> form = new HashMap<>();
                form.put("username", "test1");
                form.put("password", "password");
                try {
                    BackEndResponse response = client.postForm(form, url);
                    assertThat(response.getContentType(), is("application/json"));
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                } finally {
                    pause.countDown();
                }
            }
        };

        BackEndResponseHandler handler = new BackEndResponseHandler(tracker);
        BackEndResponse response = new BackEndResponse();
        response.setContentType("text/html");
        boolean valid = handler.validateResponse(response, url);
        assertThat(valid, is(false));
        try {
            pause.await();
        } catch (InterruptedException e) {
            Assert.fail("Pause was interrupted");
        }
    }
}