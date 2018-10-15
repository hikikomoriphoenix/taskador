package marabillas.loremar.taskador.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

/**
 * Base class for taskador's activities. It allows binding a {@link BackgroundTaskManager}
 * service to handle background tasks for the particular activity.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BackgroundServiceClient,
        BackgroundServiceConnection.OnServiceConnectedListener {
    @Override
    public void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        BackgroundServiceConnection conn = new BackgroundServiceConnection(this);
        Intent serviceIntent = new Intent(this, serviceClass);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }
}
