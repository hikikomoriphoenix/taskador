/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.ui.components.ActivityInterface;

/**
 * Base class for taskador's activities. It allows binding a {@link BackgroundTaskManager}
 * service to handle background tasks for the particular activity.
 */
public abstract class BaseActivity extends Activity implements ActivityInterface {
    private BackgroundServiceConnection backgroundServiceConnection;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass,
                                       BackgroundServiceConnection.OnServiceConnectedListener
                                               onServiceConnectedListener) {
        backgroundServiceConnection = new BackgroundServiceConnection(onServiceConnectedListener);
        Intent serviceIntent = new Intent(this, serviceClass);
        startService(serviceIntent);
        bindService(serviceIntent, backgroundServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean backgroundSupported = App.getInstance().getBackgroundTaskManagerSupport();
        if (backgroundSupported) {
            onSetupBackgroundService();
        }
    }

    @Override
    protected void onStop() {
        unbindService(backgroundServiceConnection);
        super.onStop();
    }

    /**
     * Invoked when this activity needs to set up its background service. Call
     * {@link #setupBackgroundService} and pass this activity's corresponding
     * {@link BackgroundTaskManager}.
     */
    public abstract void onSetupBackgroundService();

    @Override
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
