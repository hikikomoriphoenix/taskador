package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class MainInAppManager extends BackgroundTaskManager implements
        MainInAppBackgroundTasker {
    private MainInAppActivity mainInAppActivity;

    @Override
    public void bindActivity(MainInAppActivity activity) {
        mainInAppActivity = activity;
    }

    @Override
    public MainInAppActivity getActivity() {
        return mainInAppActivity;
    }

    @Override
    public void fetchToDoTasksList() {
        // TODO implement
    }

    @Override
    public void submitNewTask(String task) {
        // TODO implement
    }

    @Override
    public void fetchFinishedTasksList() {
        // TODO implement
    }
}
