package marabillas.loremar.taskador.ui.listeners;

import android.support.v4.view.ViewPager;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

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
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
