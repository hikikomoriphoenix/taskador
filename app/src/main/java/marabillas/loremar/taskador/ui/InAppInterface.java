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

package marabillas.loremar.taskador.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import marabillas.loremar.taskador.background.BackgroundServiceClient;
import marabillas.loremar.taskador.background.BackgroundServiceConnection;
import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;
import marabillas.loremar.taskador.ui.listeners.MainInAppRecyclerViewOnScrollChangedListener;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;

public interface InAppInterface extends BackgroundServiceClient, BackgroundServiceConnection
        .OnServiceConnectedListener, OnBackPressedInvoker {
    Context getContext();

    Window getWindow();

    void runOnUiThread(Runnable runnable);

    ToDoTasksFragment getToDoTasksFragment();

    FinishedTasksFragment getFinishedTasksFragment();

    TopWordsFragment getTopWordsFragment();

    ViewPager getPager();

    View.OnClickListener getOnClickListener();

    TextView.OnEditorActionListener getAddTaskOnEditorActionListener();

    TextWatcher getAddTaskBoxTextWatcher();

    AdapterView.OnItemSelectedListener getTopWordsNumResultsSpinnerItemSelectedListener();

    MainInAppRecyclerViewOnScrollChangedListener getRecyclerViewOnScrollListener();

    /**
     * Invoked when {@link ToDoTasksFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    void onTodoTasksWindowSelected();

    /**
     * Invoked when {@link FinishedTasksFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    void onFinishedTasksWindowSelected();

    /**
     * Invoked when {@link TopWordsFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    void onTopWordsWindowSelected();

    /**
     * Invoked when the text in the Add Task Box which is {@link ToDoTasksFragment}'s
     * {@link android.widget.EditText} for adding new tasks, is changed due to user typing on the
     * soft keyboard or text being programmatically cleared.
     *
     * @param s The new text in the Add Taskbox
     */
    void onAddTaskBoxTextChanged(Editable s);

    /**
     * Invoked when the Add Task Button, which is {@link ToDoTasksFragment}'s
     * {@link android.widget.ImageButton} for submitting new task to user's account, is clicked.
     */
    void onAddTaskButtonClicked();

    /**
     * Invoked when a new task that was inputted by the user needs to be submitted to user's
     * account.
     */
    void onAddTaskUserInput();

    /**
     * Show a dialog displaying an indeterminate horizontal progressbar to indicate that an
     * ongoing background task is being performed. This also disables user interaction until the
     * task is finished and the dialog is dismissed.
     *
     * @param stringResId resource id of the text that describes the ongoing background task
     */
    void showProgressDialog(final int stringResId);

    /**
     * Dismiss the progress dialog indicating ongoing background task. User interaction will also
     * be regained.
     */
    void dismissProgressDialog();

    void setListItemSwipeHandler(ListItemSwipeHandler listItemSwipeHandler);

    /**
     * Invoked when a list item in {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} is touched.
     *
     * @param v        list item's view
     * @param event    touch event of the list item's view
     * @param position position of the item in the list
     */
    void onListItemTouch(View v, MotionEvent event, int position);

    /**
     * Clear selection of list item.
     */
    void onListItemSelectionClear();

    /**
     * Gets the view of the item in {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} being selected.
     *
     * @return a view
     */
    View getSelectedItemView();

    /**
     * Sets the view of the item in {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} being selected.
     */
    void setSelectedItemView(View itemView);

    /**
     * Gets the position of the item in the list corresponding to the item in
     * {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} being selected.
     *
     * @return an integer
     */
    int getSelectedItemPosition();

    /**
     * Start setting the selected item as a finished task. Remove the item from the recycler view
     * and submit the task to the back-end to save it as finished.
     */
    void onTaskMarkedFinishedAction();

    /**
     * Invoked when user performs a long click on a list item in {@link ToDoTasksFragment]}.
     *
     * @param x                           x position of the touch event
     * @param y                           y position of the touch event
     * @param longClickedListItemPosition position of the long-clicked item in the list
     */
    void onToDoTaskLongClicked(final float x, final float y, final int
            longClickedListItemPosition);

    /**
     * This method is invoked when an item in {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} is swiped to enough distance to submit the
     * selected item to the back-end server to set it as excluded from the top words.
     */
    void onWordSwipedToMark();

    /**
     * This method is invoked when the user selects, via the {@link android.widget.Spinner} in
     * {@link TopWordsFragment}, a different number of results for top words.
     *
     * @param numResults desired number of results for top words
     */
    void onChangeTopWordsNumResults(int numResults);

    /**
     * This method is invoked when the user presses the {@link android.widget.Button} labeled
     * 'VIEW EXCLUDED' or 'VIEW TOP' in {@link TopWordsFragment}. This allows the user to choose
     * which, between top words list and excluded words list, to view.
     */
    void onTopWordsViewButtonClicked();

    /**
     * Invoked when the reload tool is clicked. This should re-fetch the current page's list.
     */
    void onReloadClicked();

    /**
     * Log out from account. Set current account will be cleared and you will be directed to login
     * screen.
     */
    void logout();
}
