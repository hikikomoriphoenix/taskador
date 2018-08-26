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
