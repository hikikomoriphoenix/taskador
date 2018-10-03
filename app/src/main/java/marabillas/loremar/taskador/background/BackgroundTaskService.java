package marabillas.loremar.taskador.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Service that hosts each individual activity's background task manager responsible for
 * performing background tasks.
 */
public class BackgroundTaskService extends Service {
    private LocalBinder binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        binder = new LocalBinder(this);

        return super.onStartCommand(intent, flags, startId);
    }

    // Add getters for background task managers
}
