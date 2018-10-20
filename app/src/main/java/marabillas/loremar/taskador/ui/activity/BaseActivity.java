package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

/**
 * Base class for taskador's activities. It allows binding a {@link BackgroundTaskManager}
 * service to handle background tasks for the particular activity.
 */
public abstract class BaseActivity extends Activity implements BackgroundServiceClient,
        BackgroundServiceConnection.OnServiceConnectedListener {
    @Override
    public void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        BackgroundServiceConnection conn = new BackgroundServiceConnection(this);
        Intent serviceIntent = new Intent(this, serviceClass);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }
}
