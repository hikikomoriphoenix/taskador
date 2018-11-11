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

import android.os.Binder;

/**
 * Binder for {@link BackgroundTaskManager}. This is obtained via
 * {@link android.content.ServiceConnection#onServiceConnected} upon successful use of
 * {@link android.content.Context#bindService}. Use {@link #getBackgroundTaskManager()} to get a
 * reference to the service being bound.
 */
public class LocalBinder extends Binder {
    private BackgroundTaskManager backgroundTaskManager;

    public LocalBinder(BackgroundTaskManager backgroundTaskManager) {
        this.backgroundTaskManager = backgroundTaskManager;
    }

    public BackgroundTaskManager getBackgroundTaskManager() {
        return backgroundTaskManager;
    }
}
