package marabillas.loremar.taskador.network;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import marabillas.loremar.taskador.BuildConfig;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpClientTest {

    @Test
    public void postForm() {
        String username = "test1";
        String password = "password  passsword";
        HttpClient client = new HttpClient();
        HashMap<String, String> form = new HashMap<>();
        form.put("username", username);
        form.put("password", password);
        String url = BuildConfig.backend_url + "signup.php";
        try {
            BackEndResponse response = client.postForm(form, url);
            assertThat(response.getStatusCode(), is(200));
            assertThat(response.getContentType(), containsString("text/html"));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}