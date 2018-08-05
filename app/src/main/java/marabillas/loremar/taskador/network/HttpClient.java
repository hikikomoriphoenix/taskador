package marabillas.loremar.taskador.network;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private OkHttpClient client;
    private String cookie;

    HttpClient() {
        client = new OkHttpClient();
    }

    public void postForm(Map<String, String> formData, String url) {
        FormBody.Builder fb = new FormBody.Builder();
        for (String key :
                formData.keySet()) {
            fb.add(key, formData.get(key));
        }

        RequestBody requestBody = fb.build();
        Request request = createPostRequest(url, requestBody);

        try {
            Response response = client.newCall(request).execute();
            // TODO handle response. Check if response returns a cookie and if it does, get the
            // new cookie and send the request again together with the new cookie.
        } catch (IOException e) {
            // TODO handle this exception
        }
    }

    private Request createPostRequest(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("Cookie", cookie)
                .build();
    }
}
