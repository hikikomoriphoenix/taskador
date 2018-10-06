package marabillas.loremar.taskador.ui.listeners;

import android.view.MotionEvent;
import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;

public class MainInAppOnTouchListener implements View.OnTouchListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppOnTouchListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.activity_maininapp_viewpager) {
            View selectedItemView = mainInAppActivity.getSelectedItemView();
            if (selectedItemView != null && mainInAppActivity.getSelectedItemPosition() != -1) {
                ListItemSwipeHandler swiper = mainInAppActivity.getListItemSwipeHandler();
                swiper.handleMotionEvent(selectedItemView, event);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }
        return false;
    }
}
