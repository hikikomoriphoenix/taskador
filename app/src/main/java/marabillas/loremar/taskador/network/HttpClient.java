package marabillas.loremar.taskador.network;

import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Map;

import marabillas.loremar.taskador.App;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private OkHttpClient client;

    HttpClient() {
        client = new OkHttpClient();
    }

    public BackEndResponse postForm(final Map<String, String> formData, final String
            url) {
        FormBody.Builder fb = new FormBody.Builder();
        for (String key :
                formData.keySet()) {
            fb.add(key, formData.get(key));
        }

        RequestBody requestBody = fb.build();
        Request request = createPostRequest(url, requestBody);

        return executeRequest(request);
    }

    private Request createPostRequest(String url, RequestBody requestBody) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences("config", 0);
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

    private BackEndResponse executeRequest(Request request) {
        try {
            Response response = client.newCall(request).execute();
            BackEndResponse backEndResponse = new BackEndResponse();
            backEndResponse.setStatusCode(response.code());
            backEndResponse.setContentType(response.header("Content-Type"));
            // backEndResponse.setResponseData(response.body());
            return backEndResponse;
        } catch (IOException e) {
            // TODO handle this exception
            return null;
        }
    }
}
