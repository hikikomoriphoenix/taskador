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

import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Map;

import marabillas.loremar.taskador.App;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Http client responsible for sending requests to the back-end server.
 */
public class HttpClient {
    private OkHttpClient client;
    private Call call;

    HttpClient() {
        client = new OkHttpClient();
    }

    /**
     * Send a POST request with a form.
     *
     * @param formData a Map of data for the form
     * @param url      endpoint for the request
     * @return a BackEndResponse object containing the status code, content type, and body of the
     * response
     * @throws IOException when request has failed during the process
     */
    public BackEndResponse postForm(final Map<String, String> formData, final String url) throws
            IOException {
        FormBody.Builder fb = new FormBody.Builder();
        for (String key :
                formData.keySet()) {
            fb.add(key, formData.get(key));
        }

        RequestBody requestBody = fb.build();
        Request request = createPostRequest(url, requestBody);

        return executeRequest(request);
    }

    /**
     * Send a POST request containing JSON data
     *
     * @param json string representing a JSON data
     * @param url  endpoint of the request
     * @return a BackEndResponse object containing the status code, content type, and body of the
     * response
     * @throws IOException when request has failed during the process
     */
    public BackEndResponse postJSON(String json, String url) throws IOException {
        MediaType JSON = MediaType.parse("application/json");

        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = createPostRequest(url, requestBody);

        return executeRequest(request);
    }

    private Request createPostRequest(String url, RequestBody requestBody) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences("config", 0);

        // Get the cookie received from previous attempt and include it in the headers. Once the
        // host finds the cookie in the headers, it will allow the request to get through to its
        // endpoint.
        String cookie = prefs.getString("shared_hosting_cookie", null);

        Request.Builder rb = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json");
        if (cookie != null) {
            rb.addHeader("Cookie", cookie);
        }

        return rb.build();
    }

    private BackEndResponse executeRequest(Request request) throws IOException {
        cancelRequest();
        call = client.newCall(request);
        Response response = call.execute();

        BackEndResponse backEndResponse = new BackEndResponse();
        backEndResponse.setStatusCode(response.code());
        backEndResponse.setContentType(response.header("Content-Type"));

        ResponseBody body = response.body();
        if (body != null) {
            String data = body.string();
            backEndResponse.setData(data);
        }

        return backEndResponse;
    }

    /**
     * Cancel an ongoing request.
     */
    void cancelRequest() {
        if (call != null) {
            call.cancel();
        }
    }
}
