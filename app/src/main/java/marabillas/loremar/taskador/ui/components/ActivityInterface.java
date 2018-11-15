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

package marabillas.loremar.taskador.ui.components;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

public interface ActivityInterface {
    void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass,
                                BackgroundServiceConnection.OnServiceConnectedListener
                                        onServiceConnectedListener);

    Context getContext();

    Window getWindow();

    <T extends View> T findViewById(@IdRes int id);

    void runOnUiThread(Runnable runnable);

    View getCurrentFocus();

    Object getSystemService(String name);

    /**
     * Switch to another activity.
     *
     * @param activityClass         class of the target activity.
     * @param backgroundTaskManager this activity's background service that needs to stop
     */
    void switchScreen(@NonNull Class<? extends Activity> activityClass, @NonNull
            BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input);
}
