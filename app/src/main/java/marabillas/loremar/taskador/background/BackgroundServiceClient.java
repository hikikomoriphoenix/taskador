package marabillas.loremar.taskador.background;

/**
 * Represents an object that requires another class to perform background tasks.
 */
public interface BackgroundServiceClient {
    /**
     * Bind this object to a {@link BackgroundTaskManager}
     * using a {@link android.content.Context#bindService} method.
     */
    void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass);

    /**
     * Use this for test or if using service is not suitable.
     */
    void setBackgroundTasker(BackgroundTasker backgroundTasker);
}
