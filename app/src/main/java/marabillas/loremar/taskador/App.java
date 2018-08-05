package marabillas.loremar.taskador;

import android.app.Application;

/**
 * This is the core encompassing everything in taskador. When destroyed, taskador is destroyed.
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
