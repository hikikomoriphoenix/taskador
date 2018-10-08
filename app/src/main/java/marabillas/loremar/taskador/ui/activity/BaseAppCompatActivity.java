package marabillas.loremar.taskador.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import marabillas.loremar.taskador.background.BackgroundBinder;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

public abstract class BaseAppCompatActivity extends AppCompatActivity implements BackgroundBinder,
        BackgroundServiceConnection.OnServiceConnectedListener {
    @Override
    public void bindBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        BackgroundServiceConnection conn = new BackgroundServiceConnection(this);
        Intent serviceIntent = new Intent(this, serviceClass);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }
}
