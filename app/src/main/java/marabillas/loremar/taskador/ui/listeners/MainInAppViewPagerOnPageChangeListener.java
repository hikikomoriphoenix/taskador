package marabillas.loremar.taskador.ui.listeners;

import android.support.v4.view.ViewPager;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listens to events in in-app screen's {@link ViewPager}.
 */
public class MainInAppViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppViewPagerOnPageChangeListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mainInAppActivity.onTodoTasksWindowSelected();
                break;
            case 1:
                mainInAppActivity.onFinishedTasksWindowSelected();
                break;
            case 2:
                mainInAppActivity.onTopWordsWindowSelected();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // When ViewPager is set as not being scrolled, then list items are then allowed to be
        // swiped.
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mainInAppActivity.setIsScrollingPage(false);
        }
    }
}
