package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class TodoTasksListItemSwipeHandler extends ListItemSwipeHandler {
    public TodoTasksListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.LEFT);
    }
}
