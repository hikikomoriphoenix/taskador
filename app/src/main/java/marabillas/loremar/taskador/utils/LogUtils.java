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

package marabillas.loremar.taskador.utils;

import android.util.Log;

/**
 * This utility class uses functions related to logging.
 */
public class LogUtils {
    private static final String TAG = "taskadorLog";

    private LogUtils() {
    }

    /**
     * Log a verbose message tagged with "taskador Log"
     *
     * @param message message to log
     */
    public static void log(String message) {
        Log.v(TAG, message);
    }

    /**
     * Log an error message tagged with "taskador Log"
     *
     * @param message message to log
     */
    public static void logError(String message) {
        Log.e(TAG, message);
    }

    /**
     * Log a warning message tagged with "taskador Log"
     *
     * @param message message to log
     */
    public static void logWarning(String message) {
        Log.w(TAG, message);
    }
}
