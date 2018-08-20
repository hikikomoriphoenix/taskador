package marabillas.loremar.taskador.network.tasks;

import java.lang.ref.WeakReference;

import marabillas.loremar.taskador.network.BackEndResponse;

public abstract class RunnableTask<RH extends RunnableTask.ResultHandler> implements Runnable,
        ResultListener {
    private BackEndResponse response;
    private String requestUrl;
    private WeakReference<RH> resultHandlerReference;

    public void setResultHandler(RH resultHandler) {
        resultHandlerReference = new WeakReference<>(resultHandler);
    }

    RH getResultHandler() {
        return resultHandlerReference.get();
    }

    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    BackEndResponse getResponse() {
        return response;
    }

    void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    interface ResultHandler {
    }
}
