package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 */
public class TopWordsListItemSwipeHandler extends ListItemSwipeHandler {
    public TopWordsListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.RIGHT);
    }

    @Override
    boolean checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getTopWordsFragment().getRecyclerView().getWidth();
        if (Math.abs(translation) > 0.50 * totalWidth) {
            mainInAppActivity.onWordSwipedToMark();
            return true;
        } else {
            return false;
        }
    }

    @Override
    void performActionOnMarkedItem(MainInAppActivity mainInAppActivity) {
        // TODO implement
    }
}
