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
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * A check mark image that pops up and fades out. This is used to indicate that the user choose
 * to set a task as finished.
 */
public class PopUpCheckMark extends AppCompatImageView {
    private ViewPropertyAnimatorCompat animator;

    public PopUpCheckMark(Context context) {
        super(context);

        // Create this view
        int size = getResources().getDimensionPixelSize(R.dimen
                .fragment_todotasks_popup_checkmark_size);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);
        setLayoutParams(params);
        Drawable checkmark = getResources().getDrawable(R.drawable.ic_circledcheck);
        setImageDrawable(checkmark);

        // Add view to layout
        MainInAppActivity mainInAppActivity = (MainInAppActivity) context;
        ViewGroup view = (ViewGroup) mainInAppActivity.getWindow().getDecorView().getRootView();
        if (view != null) {
            view.addView(this);
        }
        setVisibility(INVISIBLE);

        animator = ViewCompat.animate(this);
    }

    public void popUp(float x, float y) {
        ViewGroup.LayoutParams params = getLayoutParams();

        // Set initial state.
        setX(x);
        setY(y);
        setScaleX(0.1f);
        setScaleY(0.1f);

        // Get constant values for animation.
        long popUpDuration = getResources().getInteger(R.integer
                .fragment_todotasks_popupcheckmark_popupduration);
        float xBY = getResources().getInteger(R.integer
                .fragment_todotasks_popupcheckmark_translationxby);
        float yBy = getResources().getInteger(R.integer
                .fragment_todotasks_popupcheckmark_translationyby);
        final long fadeOutDuration = getResources().getInteger(R.integer
                .fragment_todotasks_popupcheckmark_fadeoutduration);

        // Pop-up animation
        setVisibility(VISIBLE);
        animator.setDuration(popUpDuration);
        animator.translationXBy(xBY - params.width);
        animator.translationYBy(yBy);
        animator.scaleX(1.0f);
        animator.scaleY(1.0f);

        animator.withEndAction(new Runnable() {
            @Override
            public void run() {
                // Fade-out animation.
                animator.setDuration(fadeOutDuration);
                animator.alpha(0);
                animator.withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Remove check mark.
                        ((ViewGroup) getParent()).removeView(PopUpCheckMark.this);
                    }
                });
            }
        });
    }
}
