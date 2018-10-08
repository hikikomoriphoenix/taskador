package marabillas.loremar.taskador.background;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public interface MainInAppBackgroundTasker extends ActivityBinder<MainInAppActivity> {
    void retrieveToDoTasksList();

    void submitNewTask(String task);
}
