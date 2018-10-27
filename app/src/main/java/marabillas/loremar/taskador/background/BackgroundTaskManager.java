package marabillas.loremar.taskador.background;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Base class for services that will perform background tasks. Use {@link #getHandler()} to get
 * its built-in {@link Handler} to post {@link Runnable}'s on a background thread.
 */
public abstract class BackgroundTaskManager extends Service {
    private LocalBinder binder;
    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new LocalBinder(this);
        }
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (handler == null) {
            HandlerThread thread = new HandlerThread("BackgroundThread");
            thread.start();
            handler = new Handler(thread.getLooper());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public Handler getHandler() {
        return handler;
    }
}
