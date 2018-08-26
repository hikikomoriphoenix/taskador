package marabillas.loremar.taskador.network.tasks;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.BackEndResponse;
import marabillas.loremar.taskador.network.HttpClient;

public abstract class RunnableTask<RH extends RunnableTask.ResultHandler> implements Runnable,
        ResultListener {
    private BackEndResponse response;
    private String requestUrl;
    private WeakReference<RH> resultHandlerReference;

    public abstract void taskIncomplete(String message);

    public void setResultHandler(RH resultHandler) {
        resultHandlerReference = new WeakReference<>(resultHandler);
    }

    RH getResultHandler() {
        return resultHandlerReference.get();
    }

    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    private void saveResult(BackEndResponse response) {
        this.response.setStatusCode(response.getStatusCode());
        this.response.setContentType(response.getContentType());
        this.response.setData(response.getData());
    }

    void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    void postForm(Map<String, String> form) throws IOException {
        BackEndAPICallTasker tasker = BackEndAPICallTasker.getInstance();
        HttpClient httpClient = tasker.getHttpClient();
        BackEndResponse backEndResponse = httpClient.postForm(form, getRequestUrl());
        saveResult(backEndResponse);
    }

    interface ResultHandler {
    }
}
