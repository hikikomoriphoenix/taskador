package marabillas.loremar.taskador.network;

import java.lang.ref.WeakReference;

abstract class RunnableTask<RH extends RunnableTask.ResultHandler> implements Runnable, ResultListener {
    BackEndResponse response;
    String url;
    private WeakReference<RH> resultHandlerReference;

    void setResultHandler(RH resultHandler) {
        resultHandlerReference = new WeakReference<>(resultHandler);
    }

    RH getResultHandler() {
        return resultHandlerReference.get();
    }

    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    public String getRequestUrl() {
        return url;
    }

    interface ResultHandler {
    }
}
