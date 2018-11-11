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

package marabillas.loremar.taskador.ui.view;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import marabillas.loremar.taskador.R;

import static android.graphics.Color.BLACK;

/**
 * A dot that can bounce once or indefinitely.
 */
public class BouncingDotView extends View {
    private ViewPropertyAnimator animator;
    private Handler handler;
    private Runnable bouncingLoop;
    private BounceRunnable bounceRunnable;
    private float mBounceHeight;
    private int bounceInterval;

    public BouncingDotView(Context context) {
        super(context);
        init();
    }

    public BouncingDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Create a black circular dot
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(BLACK);

        // Prepare view with black circular dot as background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }

        handler = new Handler(Looper.getMainLooper());

        bounceInterval = getResources().getInteger(R.integer.activity_splash_bounce_interval);
    }

    /**
     * Start bouncing in a loop.
     *
     * @param bounceHeight amount to move vertically
     */
    public void loop(float bounceHeight) {
        mBounceHeight = bounceHeight;
        animator = animate();
        bouncingLoop = new Runnable() {
            @Override
            public void run() {
                // Set dot to initially bounce upward. Bounce in the opposite direction on the
                // next call to run.
                mBounceHeight *= -1;
                animator.translationYBy(mBounceHeight);
                animator.setDuration(bounceInterval);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    animator.withEndAction(this);
                } else {
                    handler.postDelayed(this, R.integer.activity_splash_bounce_interval);
                }
            }
        };
        bouncingLoop.run();
    }

    /**
     * Bounce once. A bounce is made by moving up and then moving down back to original position.
     *
     * @param bounceHeight    amount to move up or down
     * @param bounceEndAction a runnable to execute after a full bounce is made
     */
    public void bounce(float bounceHeight, Runnable bounceEndAction) {
        mBounceHeight = bounceHeight;
        animator = animate();
        bounceRunnable = new BounceRunnable(bounceEndAction);
        bounceRunnable.run();
    }

    private class BounceRunnable implements Runnable {
        private boolean down = false;
        private Runnable bounceEndAction;

        private BounceRunnable(Runnable bounceEndAction) {
            this.bounceEndAction = bounceEndAction;
        }

        @Override
        public void run() {
            // Bounce up on first run and bounce down on the next.
            mBounceHeight *= -1;
            animator.translationYBy(mBounceHeight);
            animator.setDuration(bounceInterval);

            // Set down to true if bouncing down. If bouncing down, then bounceEndAction is set
            // to run afterwards.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (!down) {
                    down = true;
                    animator.withEndAction(this);
                } else {
                    animator.withEndAction(bounceEndAction);
                }
            } else {
                if (!down) {
                    down = true;
                    handler.postDelayed(this, bounceInterval);
                } else {
                    handler.postDelayed(bounceEndAction, bounceInterval);
                }
            }
        }
    }

    /**
     * Stop any bouncing animation
     */
    public void stop() {
        handler.removeCallbacks(bouncingLoop);
        handler.removeCallbacks(bounceRunnable);
        animator.cancel();
        setTranslationY(0);
    }
}
