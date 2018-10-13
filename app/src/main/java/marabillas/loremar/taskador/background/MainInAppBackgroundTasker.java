package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public interface MainInAppBackgroundTasker extends ActivityBinder<MainInAppActivity> {
    void fetchToDoTasksList();

    void submitNewTask(String task);

    void fetchFinishedTasksList();

    void fetchTopWordsList(int numResults);

    void fetchExcludedWordsList();
}
