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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

public interface SplashInterface extends BackgroundServiceClient,
        BackgroundServiceConnection.OnServiceConnectedListener {
    Context getSplashContext();

    void setStatusText(final String text);

    void onShowStatusFirst(final String status);

    void switchToAnotherScreen(@NonNull Class<? extends Activity> activityClass, @NonNull
            BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input);

    void finishSplash();
}
