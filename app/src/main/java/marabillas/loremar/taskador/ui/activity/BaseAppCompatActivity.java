package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import marabillas.loremar.taskador.App;
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
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        BackgroundServiceConnection conn = new BackgroundServiceConnection(this);
        Intent serviceIntent = new Intent(this, serviceClass);
        startService(serviceIntent);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean backgroundSupported = App.getInstance().getBackgroundTaskManagerSupport();
        if (backgroundSupported) {
            onSetupBackgroundService();
        }
    }

    /**
     * Invoked when this activity needs to set up its background service. Call
     * {@link #setupBackgroundService} and pass this activity's corresponding
     * {@link BackgroundTaskManager}.
     */
    public abstract void onSetupBackgroundService();

    /**
     * Switch to another activity.
     *
     * @param activityClass         class of the target activity.
     * @param backgroundTaskManager this activity's background service that needs to stop
     */
    public void switchScreen(@NonNull Class<? extends Activity> activityClass, @NonNull
            BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input) {
        // Prepare intent
        Intent intent = new Intent(this, activityClass);
        if (input != null) {
            intent.putExtra("input", input);
        }

        // Clear stack history for activities upon starting new activity. This allows to exit app
        // with back button.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Switch activity and stop old service
        startActivity(intent);
        backgroundTaskManager.stopSelf();
    }
}
