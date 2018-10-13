package marabillas.loremar.taskador.background;

public interface BackgroundServiceClient {
    void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass);

    /**
     * User this for test or if using service is not suitable.
     */
    void setBackgroundTasker(BackgroundTasker backgroundTasker);
}
