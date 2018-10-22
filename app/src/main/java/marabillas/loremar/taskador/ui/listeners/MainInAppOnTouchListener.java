package marabillas.loremar.taskador.ui.listeners;

import android.view.MotionEvent;
import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;

/**
 * Listens to touch events in in-app screen. Some components in the screen may have their own
 * listeners.
 */
public class MainInAppOnTouchListener implements View.OnTouchListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppOnTouchListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.activity_maininapp_viewpager) {
            // Handle swipe motion if a list item is selected.
            View selectedItemView = mainInAppActivity.getSelectedItemView();
            if (selectedItemView != null && mainInAppActivity.getSelectedItemPosition() != -1) {
                ListItemSwipeHandler swiper = mainInAppActivity.getListItemSwipeHandler();
                swiper.handleMotionEvent(selectedItemView, event);
            }

            // If a list item is being swiped, disallow scrolling of the ViewPager. By returning
            // true, this will not proceed to handling scroll events for the ViewPager.
            return mainInAppActivity.isSwipingItem();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }
        return false;
    }
}
