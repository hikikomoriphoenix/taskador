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

package marabillas.loremar.taskador;

import android.app.Application;

import marabillas.loremar.taskador.background.BackgroundTaskManager;

/**
 * This is the core encompassing everything in taskador. When destroyed, taskador is destroyed.
 */
public class App extends Application {
    private static App instance;
    private boolean backgroundTaskManagerSupport;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    /**
     * If set to true, taskador's activities will use a {@link BackgroundTaskManager} to perform
     * its background tasks.
     *
     * @param support value to set with
     */
    public void setBackgroundTaskManagerSupport(boolean support) {
        backgroundTaskManagerSupport = support;
    }

    public boolean getBackgroundTaskManagerSupport() {
        return backgroundTaskManagerSupport;
    }
}
