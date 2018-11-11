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
