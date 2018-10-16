package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 */
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
