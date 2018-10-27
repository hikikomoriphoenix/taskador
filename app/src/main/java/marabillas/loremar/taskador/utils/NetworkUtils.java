package marabillas.loremar.taskador.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import marabillas.loremar.taskador.App;

/**
 * Network-related helper methods
 */
public final class NetworkUtils {
    private NetworkUtils() {
    }

    /**
     * Check if device is connected to a network.
     *
     * @return true or false
     */
    public static boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context
                .CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
