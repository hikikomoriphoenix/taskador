package marabillas.loremar.taskador.ui.motion;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public abstract class ListItemSwipeHandler {
    private float x0;
    private StartPosition startPosition;
    private MainInAppActivity mainInAppActivity;
    private NoSwipeTimer noSwipeTimer;

    public enum StartPosition {LEFT, RIGHT}

    ListItemSwipeHandler(MainInAppActivity mainInAppActivity, StartPosition startPosition) {
        this.startPosition = startPosition;
        this.mainInAppActivity = mainInAppActivity;
    }

    public void handleMotionEvent(View v, MotionEvent motionEvent) {
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
                float x1 = motionEvent.getRawX();
                float d = x1 - x0;
                x0 = x1;

                float itemViewTranslation = v.getTranslationX();
                float targetItemViewTranslation = itemViewTranslation + d;

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

                v.setTranslationX(targetItemViewTranslation);

                // Check if item is swiped to enough distance to mark it as checked
                float totalWidth = mainInAppActivity.getToDoTasksFragment().getRecyclerView()
                        .getWidth();
                if (Math.abs(targetItemViewTranslation) > 0.50 * totalWidth) {
                    mainInAppActivity.onMarkTaskChecked();
                }

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
            finishSwipe(selectedItemView);
        }
    }
}
