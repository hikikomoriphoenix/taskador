package marabillas.loremar.taskador.background;

import android.app.Activity;

public interface BackgroundTasker<A extends Activity> {
    void bindActivity(A activity);

    A getActivity();
}
