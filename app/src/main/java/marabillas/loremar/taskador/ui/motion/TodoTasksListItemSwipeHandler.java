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

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Handles swipe motion for the list items in
 * {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * {@link android.support.v7.widget.RecyclerView}.
 */
public class TodoTasksListItemSwipeHandler extends ListItemSwipeHandler {
    public TodoTasksListItemSwipeHandler(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity, StartPosition.LEFT);
    }

    @Override
    boolean checkIfSwipedToMark(MainInAppActivity mainInAppActivity, float translation) {
        float totalWidth = mainInAppActivity.getToDoTasksFragment().getRecyclerView().getWidth();
        View selectedItemView = mainInAppActivity.getSelectedItemView();
        ImageView check = null;
        if (selectedItemView != null) {
            check = selectedItemView.findViewById(R.id.fragment_todotasks_listitem_check);
        }

        if (Math.abs(translation) > 0.50 * totalWidth) {
            if (check != null) {
                Drawable checkDrawable = mainInAppActivity.getResources().getDrawable(R.drawable.ic_checked);
                check.setImageDrawable(checkDrawable);
            }
            return true;
        } else {
            if (check != null) {
                Drawable notCheckedDrawable = mainInAppActivity.getResources().getDrawable(R.drawable
                        .ic_notchecked);
                check.setImageDrawable(notCheckedDrawable);
            }
            return false;
        }
    }

    @Override
    void performActionOnMarkedItem(MainInAppActivity mainInAppActivity) {
        mainInAppActivity.onTaskMarkedFinishedAction();
        View selectedItemView = mainInAppActivity.getSelectedItemView();
        ImageView check = selectedItemView.findViewById(R.id.fragment_todotasks_listitem_check);
        Drawable notCheckedDrawable = mainInAppActivity.getResources().getDrawable(R.drawable
                .ic_notchecked);
        check.setImageDrawable(notCheckedDrawable);
    }
}
