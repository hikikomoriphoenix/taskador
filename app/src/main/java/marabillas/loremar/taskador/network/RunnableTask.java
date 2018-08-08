package marabillas.loremar.taskador.network;

abstract class RunnableTask implements Runnable, ResultListener {
    BackEndResponse response;
    String url;

    abstract void setResultHandler(ResultHandler resultHandler);

    public void trackForResult(BackEndResponse response) {
        this.response = response;
    }

    public String getRequestUrl() {
        return url;
    }

    interface ResultHandler {
    }
}
