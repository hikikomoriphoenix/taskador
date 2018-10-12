package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class TodoTasksListItemSwipeHandler extends ListItemSwipeHandler {
    public TodoTasksListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.LEFT);
    }

    @Override
    void checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getToDoTasksFragment().getRecyclerView().getWidth();
        if (Math.abs(translation) > 0.50 * totalWidth) {
            mainInAppActivity.onMarkTaskChecked();
        }
    }
}
