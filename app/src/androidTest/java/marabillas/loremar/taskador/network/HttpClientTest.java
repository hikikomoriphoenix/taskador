/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
        String url = BuildConfig.backend_url + "account/signup.php";
        try {
            BackEndResponse response = client.postForm(form, url);
            assertThat(response.getStatusCode(), is(200));
            assertThat(response.getContentType(), containsString("text/html"));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void postJSON() {
        String json = "{}";
        HttpClient client = new HttpClient();
        String url = BuildConfig.backend_url + "words/get-top-words.php";
        try {
            BackEndResponse response = client.postJSON(json, url);
            assertThat(response.getStatusCode(), is(200));
            assertThat(response.getContentType(), containsString("text/html"));
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}