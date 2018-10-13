package marabillas.loremar.taskador.ui.motion;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class TopWordsListItemSwipeHandler extends ListItemSwipeHandler {
    public TopWordsListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.RIGHT);
    }

    @Override
    void checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getTopWordsFragment().getRecyclerView().getWidth();
        if (Math.abs(translation) > 0.50 * totalWidth) {
            mainInAppActivity.onWordSwipedToMark();
        }
    }
}
