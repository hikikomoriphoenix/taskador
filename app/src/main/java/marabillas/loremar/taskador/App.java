package marabillas.loremar.taskador;

import android.app.Application;

import marabillas.loremar.taskador.background.BackgroundTaskManager;

/**
 * This is the core encompassing everything in taskador. When destroyed, taskador is destroyed.
 */
public class App extends Application {
    private static App instance;
    private boolean backgroundTaskManagerSupport;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    /**
     * If set to true, taskador's activities will use a {@link BackgroundTaskManager} to perform
     * its background tasks.
     *
     * @param support value to set with
     */
    public void setBackgroundTaskManagerSupport(boolean support) {
        backgroundTaskManagerSupport = support;
    }

    public boolean getBackgroundTaskManagerSupport() {
        return backgroundTaskManagerSupport;
    }
}
