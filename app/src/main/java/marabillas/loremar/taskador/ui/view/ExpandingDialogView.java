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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.components.OnBackPressedInvoker;
import marabillas.loremar.taskador.ui.components.OnBackPressedListener;

/**
 * A dialog that pops up from a choosen point in the screen and expands.
 */
public class ExpandingDialogView extends LinearLayout implements Animator.AnimatorListener, View
        .OnClickListener, OnBackPressedListener {
    private Context context;
    private FrameLayout backgroundView;
    private View content;
    private Button closeButton;

    /**
     * @param context context required to create views
     * @param parent  the parent view group to contain this dialog
     */
    public ExpandingDialogView(Context context, ViewGroup parent) {
        super(context);
        this.context = context;

        // Add an opaque background to the dialog
        backgroundView = new FrameLayout(context);
        ViewGroup.LayoutParams backgroundViewParams = new ViewGroup.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        backgroundView.setLayoutParams(backgroundViewParams);
        backgroundView.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        parent.addView(backgroundView);

        // Add the dialog
        ViewGroup.LayoutParams dialogParams = new ViewGroup.LayoutParams(0, 0);
        setLayoutParams(dialogParams);
        int color = getResources().getColor(R.color
                .acitvity_maininapp_expandingdialogview_background);
        setBackgroundDrawable(new ColorDrawable(color));
        setOrientation(VERTICAL);
        parent.addView(this);

        backgroundView.setOnClickListener(this);
        setOnClickListener(this);
    }

    public ExpandingDialogView(Context context) {
        super(context);
    }

    public void show(int startX, int startY, View content) {
        this.content = content;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int elevation = getResources().getInteger(R.integer.expandingdialogview_elevation);
            setElevation(elevation);
        }

        setX(startX);
        setY(startY);
        setLeft(startX);
        setTop(startY);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int margin = getResources().getInteger(R.integer.expandingdialogview_margin);
        int topMargin = getResources().getInteger(R.integer.expandingdialogview_topmargin);

        int endRight = screenWidth - margin;
        int endBottom = screenHeight - margin;

        // The x property represents the leftmost position of the visible pixels while the
        // left property represents the left boundary of the view. Therefore, in order for the
        // visible pixels of the view to always coincide with the boundaries of the view, the x
        // property needs to move along with the left property. This allows the displayed dialog
        // to expand as its boundaries expand. To do this, translationX property is made sure to be
        // always equal to zero in the whole duration of the animation. This is because, the
        // translationX property represents the difference between the X property and the left
        // property.
        ObjectAnimator leftMove = ObjectAnimator.ofInt(this, "left", getLeft(), margin);
        ObjectAnimator topMove = ObjectAnimator.ofInt(this, "top", getTop(), topMargin);
        ObjectAnimator rightMove = ObjectAnimator.ofInt(this, "right", getLeft(), endRight);
        ObjectAnimator bottomMove = ObjectAnimator.ofInt(this, "bottom", getTop(), endBottom);
        ObjectAnimator xMove = ObjectAnimator.ofFloat(this, "translationX", 0, 0);
        ObjectAnimator yMove = ObjectAnimator.ofFloat(this, "translationY", 0, 0);
        ObjectAnimator backgroundFadeIn = ObjectAnimator.ofFloat(backgroundView, "alpha", 0, 0.5f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftMove, topMove, rightMove, bottomMove, xMove,
                yMove, backgroundFadeIn);
        int duration = getResources().getInteger(R.integer.expandingdialogview_duration);
        animatorSet.setDuration(duration);
        animatorSet.addListener(this);
        animatorSet.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        // When animation ends, the view is restored to its layout properties. To make sure the
        // current values of the view properties are kept, we set the value of the layout
        // properties to coincide with these current values. The view will then be restored to
        // these new values.
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        int margin = getResources().getInteger(R.integer.expandingdialogview_margin);
        int topMargin = getResources().getInteger(R.integer.expandingdialogview_topmargin);
        params.leftMargin = margin;
        params.topMargin = topMargin;
        params.rightMargin = margin;
        params.bottomMargin = margin;


        // Add content for dialog
        LayoutParams contentParams = new LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, 0);
        contentParams.weight = 1;
        content.setLayoutParams(contentParams);
        int padding = context.getResources().getDimensionPixelSize(R.dimen
                .activity_maininapp_expandingdialog_textpadding);
        content.setPadding(padding, padding, padding, padding);
        addView(content);

        // Add close button
        closeButton = new Button(context);
        LayoutParams buttonParams = new LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        closeButton.setLayoutParams(buttonParams);
        closeButton.setText(R.string.activity_maininapp_expandingDialogClose);
        buttonParams.gravity = Gravity.CENTER;
        addView(closeButton);

        closeButton.setOnClickListener(this);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onClick(View v) {
        if (v == closeButton) {
            dismiss();
        }
    }

    // Dismiss this dialog, removing all of its views from the parent layout
    public void dismiss() {
        ViewGroup parent = (ViewGroup) getParent();
        parent.removeView(backgroundView);
        parent.removeView(this);
    }

    @Override
    public void onBackPressed(OnBackPressedInvoker onBackPressedInvoker) {
        onBackPressedInvoker.setOnBackPressedListener(null);
        dismiss();
    }
}
