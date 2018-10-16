package marabillas.loremar.taskador.background;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * ServiceConnection used for binding a {@link BackgroundTaskManager} service. Pass an instance
 * of this class to the {@link android.content.Context#bindService} method when binding a
 * {@link BackgroundTaskManager}. Reference to the BackgroundTaskManager service is obtained
 * through {@link OnServiceConnectedListener} passed to this class's constructor, upon successful
 * binding.
 */
public class BackgroundServiceConnection implements ServiceConnection {
    private OnServiceConnectedListener onServiceConnectedListener;

    public BackgroundServiceConnection(OnServiceConnectedListener onServiceConnectedListener) {
        this.onServiceConnectedListener = onServiceConnectedListener;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LocalBinder binder = (LocalBinder) service;
        onServiceConnectedListener.onServiceConnected(binder.getBackgroundTaskManager());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public interface OnServiceConnectedListener {
        /**
         * This method is invoked upon successful binding of
         * {@link marabillas.loremar.taskador.background.BackgroundTaskManager} using the
         * {@link android.content.Context#bindService} method.
         *
         * @param backgroundTaskManager reference to
         *                              {@link marabillas.loremar.taskador.background.BackgroundTaskManager} service
         */
        void onServiceConnected(BackgroundTaskManager backgroundTaskManager);
    }
}
