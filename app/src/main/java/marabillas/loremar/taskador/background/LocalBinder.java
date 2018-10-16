package marabillas.loremar.taskador.background;

import android.os.Binder;

/**
 * Binder for {@link BackgroundTaskManager}. This is obtained via
 * {@link android.content.ServiceConnection#onServiceConnected} upon successful use of
 * {@link android.content.Context#bindService}. Use {@link #getBackgroundTaskManager()} to get a
 * reference to the service being bound.
 */
public class LocalBinder extends Binder {
    private BackgroundTaskManager backgroundTaskManager;

    public LocalBinder(BackgroundTaskManager backgroundTaskManager) {
        this.backgroundTaskManager = backgroundTaskManager;
    }

    public BackgroundTaskManager getBackgroundTaskManager() {
        return backgroundTaskManager;
    }
}
