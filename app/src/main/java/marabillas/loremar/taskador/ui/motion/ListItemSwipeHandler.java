/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package marabillas.loremar.taskador.ui.motion;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.ui.InAppInterface;

import static android.support.v4.view.ViewCompat.animate;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * and {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 *
 * TODO Mechanism of List Item Swipe:
 */
public abstract class ListItemSwipeHandler {
    private float x0;
    private long t0;
    private float velocity;

    private StartPosition startPosition;
    private InAppInterface mainInApp;

    /**
     * A list item's initial position indicating from which position the swipe motion starts from.
     */
    public enum StartPosition {LEFT, RIGHT}

    ListItemSwipeHandler(InAppInterface mainInApp, StartPosition startPosition) {
        this.startPosition = startPosition;
        this.mainInApp = mainInApp;
    }

    /**
     * Handles events on a list item to produce swipe motion.
     *
     * @param v           the view of the selected or touched list item
     * @param motionEvent the event caught by the list item's view's
     * {@link android.view.View.OnTouchListener}.
     */
    public void handleMotionEvent(View v, MotionEvent motionEvent) {
        mainInApp.runOnUiThread(new HandleMotionEventRunnable(v, motionEvent));
    }

    private class HandleMotionEventRunnable implements Runnable, DynamicAnimation.OnAnimationEndListener {
        private View v;
        private MotionEvent motionEvent;

        private HandleMotionEventRunnable(View v, MotionEvent motionEvent) {
            this.v = v;
            this.motionEvent = motionEvent;
        }

        @Override
        public void run() {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x0 = motionEvent.getRawX();
                    t0 = System.nanoTime();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // Get horizontal movement and update initial position for next calculation
                    float x1 = motionEvent.getRawX();
                    float d = x1 - x0;
                    x0 = x1;

                    // Calculate velocity
                    long t1 = System.nanoTime();
                    long dt = t1 - t0;
                    velocity = d / (float) dt;
                    t0 = t1;

                    // The item's view needs to move in the same amount of horizontal movement.
                    // Get the final position of the view.
                    float itemViewTranslation = v.getTranslationX();
                    float targetItemViewTranslation = itemViewTranslation + d;

                    // If movement is towards the start position, make sure the item doesn't move
                    // past it.
                    if (startPosition == StartPosition.LEFT && targetItemViewTranslation < 0) {
                        v.setTranslationX(0);
                        mainInApp.onListItemSelectionClear();
                        // Allow ViewPager to scroll right.
                        mainInApp.getPager().requestDisallowInterceptTouchEvent(false);
                        return;
                    } else if (startPosition == StartPosition.RIGHT && targetItemViewTranslation >
                            0) {
                        v.setTranslationX(0);
                        mainInApp.onListItemSelectionClear();
                        // Allow ViewPager to scroll left.
                        mainInApp.getPager().requestDisallowInterceptTouchEvent(false);
                        return;
                    }

                    // Move item to final position.
                    v.setTranslationX(targetItemViewTranslation);

                    checkIfSwipedToMark(mainInApp, targetItemViewTranslation);
                    break;
                case MotionEvent.ACTION_UP:
                    if (v.getTranslationX() != 0) {
                        finishSwipe();
                    }
                    break;
                default:
                    if (v.getTranslationX() != 0) {
                        finishSwipe();
                    }
            }
        }

        /**
         * End the swipe motion with some fling animation.
         */
        private void finishSwipe() {
            Context context = mainInApp.getContext();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            float velocityInPixelsPerSecond = velocity * TimeUnit.SECONDS.toNanos(1);
            FlingAnimation flingAnimation = new FlingAnimation(v, DynamicAnimation.TRANSLATION_X)
                    .setStartVelocity(velocityInPixelsPerSecond);

            boolean startsLeft = startPosition == StartPosition.LEFT;
            boolean startsRight = startPosition == StartPosition.RIGHT;

            // Limit animation to within the screen using setMinValue and setMaxValue.
            if (startsLeft) {
                flingAnimation.setMinValue(0).setMaxValue((float) screenWidth);
            } else {
                flingAnimation.setMinValue(-((float) screenWidth)).setMaxValue(0);
            }

            // If the item is set to move towards its original position, lessen the fling
            // animation's friction.
            if ((startsLeft && velocity <= 0) || (startsRight && velocity >= 0)) {
                flingAnimation.setFriction(0.1f);
            }

            mainInApp.getRecyclerViewOnScrollListener().setKeepingSelection(true);
            flingAnimation.addEndListener(this);
            flingAnimation.start();
        }

        @Override
        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
            // Check if item is flung to mark. This allows the user to fling an item to marking
            // position instead of just dragging the item towards it.
            boolean swipedToMark = checkIfSwipedToMark(mainInApp, v.getTranslationX());

            if (swipedToMark) {
                moveItemOffScreen();
            } else {
                moveItemBackToOriginalPosition();
            }

            mainInApp.getRecyclerViewOnScrollListener().setKeepingSelection(false);
        }

        private void moveItemBackToOriginalPosition() {
            mainInApp.onListItemSelectionClear();
            ViewPropertyAnimator animator = v.animate();
            animator.setDuration(500);
            animator.translationX(0);
        }

        private void moveItemOffScreen() {
            int totalWidth = mainInApp.getToDoTasksFragment().getRecyclerView().getWidth();

            // The compat version is used since withEndAction doesn't seemed to be supported in
            // older versions.
            ViewPropertyAnimatorCompat animatorCompat = animate(v);
            animatorCompat.setDuration(100);

            if (startPosition == StartPosition.LEFT) {
                animatorCompat.translationX(totalWidth);
            } else {
                animatorCompat.translationX(-totalWidth);
            }

            animatorCompat.withEndAction(new Runnable() {
                @Override
                public void run() {
                    performActionOnMarkedItem(mainInApp);
                    mainInApp.onListItemSelectionClear();
                }
            });
        }
    }

    /**
     * Check if the list item is swiped to a certain enough distance. If it is, then the item is
     * marked for action.
     *
     * @param mainInApp in-app screen's interface
     * @param translation the current value of the list item's view's translation property
     *
     * @return true or false
     */
    abstract boolean checkIfSwipedToMark(InAppInterface mainInApp, float translation);

    abstract void performActionOnMarkedItem(InAppInterface mainInApp);
}
