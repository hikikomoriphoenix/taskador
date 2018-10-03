package marabillas.loremar.taskador.background;

import android.app.Service;
import android.os.Binder;

/**
 * This class allows access to each Activity's background task manager hosted by
 * BackgroundTaskService. An instance of this object can be acquired by creating a ServiceConnection
 * and calling an activity's bindService method which in turn invokes the ServiceConnection's
 * onServiceConnected method.
 */
public class LocalBinder extends Binder {
    private Service service;

    public LocalBinder(Service service) {
        this.service = service;
    }

    // Add public getters for individual Activity managers
}
