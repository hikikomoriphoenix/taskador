package marabillas.loremar.taskador.ui.motion;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import marabillas.loremar.taskador.R;
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
    boolean checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getToDoTasksFragment().getRecyclerView().getWidth();
        View selectedItemView = mainInAppActivity.getSelectedItemView();
        ImageView check = selectedItemView.findViewById(R.id.fragment_todotasks_listitem_check);
        if (Math.abs(translation) > 0.50 * totalWidth) {
            Drawable checkDrawable = mainInAppActivity.getResources().getDrawable(R.drawable.ic_checked);
            check.setImageDrawable(checkDrawable);
            mainInAppActivity.setTaskMarkedFinished(true);
            return true;
        } else {
            Drawable notCheckedDrawable = mainInAppActivity.getResources().getDrawable(R.drawable
                    .ic_notchecked);
            check.setImageDrawable(notCheckedDrawable);
            mainInAppActivity.setTaskMarkedFinished(false);
            return false;
        }
    }

    @Override
    void performActionOnMarkedItem(MainInAppActivity mainInAppActivity) {
        mainInAppActivity.onTaskMarkedFinishedAction();
    }
}
