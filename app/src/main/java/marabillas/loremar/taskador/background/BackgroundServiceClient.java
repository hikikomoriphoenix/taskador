package marabillas.loremar.taskador.background;

/**
 * Represents an object that requires another class to perform background tasks.
 */
public interface BackgroundServiceClient {
    /**
     * Start a {@link BackgroundTaskManager} and bind it to this object
     * using a {@link android.content.Context#bindService} method.
     */
    void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass);

    /**
     * Use this for test or if using service is not suitable.
     */
    void setBackgroundTasker(BackgroundTasker backgroundTasker);
}
