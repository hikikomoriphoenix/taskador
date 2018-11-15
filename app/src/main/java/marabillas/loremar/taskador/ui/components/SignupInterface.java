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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.background.BackgroundTaskManager;

public interface SignupInterface extends BackgroundServiceClient,
        BackgroundServiceConnection.OnServiceConnectedListener {
    void onConfirmPasswordBoxTextChanged(final String text);

    /**
     * Method invoked when inputs need to be submitted to register new account. Inputs are first
     * checked if they are valid before submitting.
     */
    void onSubmit();

    void onPasswordBoxTextChanged(final String text);

    void onUsernameBoxTextChanged(final String text);

    void onUsernameIsAvailable();

    void onUsernameNotAvailable();

    void switchToAnotherScreen(@NonNull Class<? extends Activity> activityClass, @NonNull
            BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input);
}
