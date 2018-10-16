package marabillas.loremar.taskador.ui.motion;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * and {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 */
public abstract class ListItemSwipeHandler {
    private float x0;
    private StartPosition startPosition;
    private MainInAppActivity mainInAppActivity;
    private NoSwipeTimer noSwipeTimer;

    /**
     * A list item's initial position indicating from which position the swipe motion starts from.
     */
    public enum StartPosition {LEFT, RIGHT}

    ListItemSwipeHandler(MainInAppActivity mainInAppActivity, StartPosition startPosition) {
        this.startPosition = startPosition;
        this.mainInAppActivity = mainInAppActivity;
    }

    /**
     * Handles events on a list item related to swipe motion
     *
     * @param v           the view of the selected or touched list item
     * @param motionEvent the event caught by
     *                    {@link marabillas.loremar.taskador.ui.listeners.MainInAppOnTouchListener} or the list
     *                    item's view's {@link android.view.View.OnTouchListener}.
     */
    public void handleMotionEvent(View v, MotionEvent motionEvent) {
        mainInAppActivity.runOnUiThread(new HandleMotionEventRunnable(v, motionEvent));
    }

    private class HandleMotionEventRunnable implements Runnable {
        private View v;
        private MotionEvent motionEvent;

        private HandleMotionEventRunnable(View v, MotionEvent motionEvent) {
            this.v = v;
            this.motionEvent = motionEvent;
        }

        @Override
        public void run() {
            // Set up a countdown timer to track the time elapsed when there are no swipe-related
            // events. When the timer completes its countdown, the list item is set released.
            if (noSwipeTimer != null) {
                noSwipeTimer.cancel();
                noSwipeTimer.start();
            } else {
                noSwipeTimer = new NoSwipeTimer(500, 500, v);
            }

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x0 = motionEvent.getRawX();
                    v.clearAnimation();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Get horizontal movement and update initial position for next calculation
                    float x1 = motionEvent.getRawX();
                    float d = x1 - x0;
                    x0 = x1;

                    // The item's view needs to move in the same amount of horizontal movement.
                    // Get the final position of the view.
                    float itemViewTranslation = v.getTranslationX();
                    float targetItemViewTranslation = itemViewTranslation + d;

                    // If movement is towards the start position, make sure the item doesn't move
                    // past it.
                    if (startPosition == StartPosition.LEFT) {
                        if (targetItemViewTranslation < 0) {
                            v.setTranslationX(0);
                            return;
                        }
                    } else {
                        if (targetItemViewTranslation > 0) {
                            v.setTranslationX(0);
                            return;
                        }
                    }

                    // Move item to final position.
                    v.setTranslationX(targetItemViewTranslation);

                    checkIfSwipedToMark(mainInAppActivity, targetItemViewTranslation);
                    break;
                case MotionEvent.ACTION_UP:
                    finishSwipe(v);
                    break;

                default:
                    if (v.getTranslationX() != 0) {
                        finishSwipe(v);
                    }
            }
        }
    }

    /**
     * If the item is not marked for action, the swipe motion is finished by moving the item to
     * starting position and set it as released.
     *
     * @param v the selected item's view.
     */
    private void finishSwipe(View v) {
        moveItemBackToOriginalPosition(v);
        mainInAppActivity.onListItemRelease();
    }

    private void moveItemBackToOriginalPosition(View itemView) {
        ViewPropertyAnimator animator = itemView.animate();
        animator.setDuration(500);
        animator.translationX(0);
    }

    private class NoSwipeTimer extends CountDownTimer {
        private View selectedItemView;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        private NoSwipeTimer(long millisInFuture, long countDownInterval, View selectedItemView) {
            super(millisInFuture, countDownInterval);
            this.selectedItemView = selectedItemView;
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finishSwipe(selectedItemView);
                }
            });
        }
    }

    /**
     * Check if the list item is swiped to a certain enough distance. If it is, then the item is
     * marked for action.
     *
     * @param mainInAppActivity the in-app screen's activity
     * @param translation the current value of the list item's view's translation property
     */
    abstract void checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation);
}
