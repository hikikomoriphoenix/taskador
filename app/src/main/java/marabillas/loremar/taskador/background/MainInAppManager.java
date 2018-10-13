package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class MainInAppManager extends BackgroundTaskManager implements
        MainInAppBackgroundTasker {
    private MainInAppActivity mainInAppActivity;

    @Override
    public void bindClient(MainInAppActivity client) {
        mainInAppActivity = client;
    }

    @Override
    public MainInAppActivity getClient() {
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

    @Override
    public void fetchTopWordsList(int numResults) {
        // TODO implement
    }

    @Override
    public void fetchExcludedWordsList() {
        // TODO implement
    }
}
