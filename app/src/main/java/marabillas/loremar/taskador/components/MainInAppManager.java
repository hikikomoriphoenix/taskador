package marabillas.loremar.taskador.components;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class MainInAppManager implements MainInAppEventHandler {
    private MainInAppActivity mainInAppActivity;

    @Override
    public void onMainInAppIsReady(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
        setupTodoTasksWindow();
    }

    @Override
    public void onTodoTasksWindowSelected() {
        setupTodoTasksWindow();
    }

    @Override
    public void onAddNewTask(String task) {
        // TODO handle onAddNewTask event
    }

    private void setupTodoTasksWindow() {
        // TODO implement setupTodoTasksWindow
    }
}
