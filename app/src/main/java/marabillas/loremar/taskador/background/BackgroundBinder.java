package marabillas.loremar.taskador.background;

public interface BackgroundBinder {
    void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass);

    /**
     * User this for test or if using service is not suitable.
     */
    void setBackgroundTasker(ActivityBinder activityBinder);
}
