package marabillas.loremar.taskador.background;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

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

    interface OnServiceConnectedListener {
        void onServiceConnected(BackgroundTaskManager backgroundTaskManager);
    }
}
