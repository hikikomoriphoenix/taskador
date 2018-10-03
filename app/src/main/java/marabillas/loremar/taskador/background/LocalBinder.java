package marabillas.loremar.taskador.background;

import android.os.Binder;

public class LocalBinder extends Binder {
    private BackgroundTaskManager backgroundTaskManager;

    public LocalBinder(BackgroundTaskManager backgroundTaskManager) {
        this.backgroundTaskManager = backgroundTaskManager;
    }

    public BackgroundTaskManager getBackgroundTaskManager() {
        return backgroundTaskManager;
    }
}
