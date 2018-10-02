package marabillas.loremar.taskador.components;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public interface MainInAppEventHandler {
    void onMainInAppIsReady(MainInAppActivity mainInAppActivity);

    void onTodoTasksWindowSelected();

    void onAddNewTask(String task);
}
