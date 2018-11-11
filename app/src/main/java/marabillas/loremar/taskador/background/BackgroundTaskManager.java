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
