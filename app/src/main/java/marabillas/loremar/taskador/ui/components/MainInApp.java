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

package marabillas.loremar.taskador.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.MainInAppBackgroundTasker;
import marabillas.loremar.taskador.background.MainInAppManager;
import marabillas.loremar.taskador.background.SplashBackgroundTasker;
import marabillas.loremar.taskador.ui.activity.SplashActivity;
import marabillas.loremar.taskador.ui.adapter.MainInappViewPagerAdapter;
import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;
import marabillas.loremar.taskador.ui.listeners.AddTaskBoxTextWatcher;
import marabillas.loremar.taskador.ui.listeners.AddTaskOnEditorActionListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppOnClickListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppRecyclerViewOnScrollChangedListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppViewPagerOnPageChangeListener;
import marabillas.loremar.taskador.ui.listeners.TopWordsNumResultsSpinnerItemSelectedListener;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TodoTasksListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TopWordsListItemSwipeHandler;
import marabillas.loremar.taskador.ui.view.PopUpCheckMark;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MainInApp implements InAppInterface, OnBackPressedInvoker {
    private ActivityCompatInterface activityInterface;

    private ToDoTasksFragment toDoTasksFragment;
    private FinishedTasksFragment finishedTasksFragment;
    private TopWordsFragment topWordsFragment;
    private ViewPager pager;
    private MainInAppBackgroundTasker mainInAppBackgroundTasker;
    private ListItemSwipeHandler listItemSwipeHandler;

    // Listeners
    private View.OnClickListener onClickListener;
    private TextView.OnEditorActionListener addTaskOnEditorActionListener;
    private TextWatcher addTaskBoxTextWatcher;
    private AdapterView.OnItemSelectedListener topWordsNumResultsSpinnerItemSelectedListener;
    private OnBackPressedListener onBackPressedListener;
    private MainInAppRecyclerViewOnScrollChangedListener recyclerViewOnScrollListener;

    private View selectedItemView;
    private int selectedItemPosition;

    private AlertDialog progressDialog;
    private TextView progressDialogTextView;
    private View deleteBubbleView;

    public MainInApp(ActivityCompatInterface activityInterface) {
        this.activityInterface = activityInterface;
    }

    public void setUp() {
        setupViewPager();

        // Initialize listeners
        onClickListener = new MainInAppOnClickListener(this);
        addTaskOnEditorActionListener = new AddTaskOnEditorActionListener(this);
        addTaskBoxTextWatcher = new AddTaskBoxTextWatcher(this);
        topWordsNumResultsSpinnerItemSelectedListener = new
                TopWordsNumResultsSpinnerItemSelectedListener(this);
        recyclerViewOnScrollListener = new MainInAppRecyclerViewOnScrollChangedListener(this);

        selectedItemView = null;
        selectedItemPosition = -1;

        // Prepare a progress dialog which will indicate ongoing background task.
        View progressDialogView = View.inflate(getContext(), R.layout.activity_maininapp_progress, null);
        progressDialog = new AlertDialog.Builder(getContext())
                .setView(progressDialogView)
                .setCancelable(false)
                .create();
        progressDialogTextView = progressDialogView.findViewById(R.id
                .activity_maininapp_progress_textview);

        // Get tools for views and set onClickListener
        TextView reload = activityInterface.findViewById(R.id.activtiy_maininapp_reloadtool);
        TextView help = activityInterface.findViewById(R.id.activity_maininapp_helptool);
        TextView about = activityInterface.findViewById(R.id.activity_maininapp_abouttool);
        TextView logout = activityInterface.findViewById(R.id.activity_maininapp_logouttool);

        reload.setOnClickListener(onClickListener);
        help.setOnClickListener(onClickListener);
        about.setOnClickListener(onClickListener);
        logout.setOnClickListener(onClickListener);
    }

    private void setupViewPager() {
        FragmentManager fm = activityInterface.getSupportFragmentManager();
        pager = activityInterface.findViewById(R.id.activity_maininapp_viewpager);

        // Instantiate fragments
        toDoTasksFragment = new ToDoTasksFragment();
        finishedTasksFragment = new FinishedTasksFragment();
        topWordsFragment = new TopWordsFragment();

        // Add ToDoTasksFragment if it does not exist.
        Fragment fragment = fm.findFragmentByTag("TODO_TASKS");
        if (fragment != null) {
            toDoTasksFragment = (ToDoTasksFragment) fragment;
        } else {
            fm.beginTransaction().add(pager.getId(), toDoTasksFragment, "TODO_TASKS").commit();
        }

        // Add FinishedTasksFragment if it does not exist.
        fragment = fm.findFragmentByTag("FINISHED_TASKS");
        if (fragment != null) {
            finishedTasksFragment = (FinishedTasksFragment) fragment;
        } else {
            fm.beginTransaction().add(pager.getId(), finishedTasksFragment, "FINISHED_TASKS")
                    .commit();
        }

        // Add TopWordsFragment if it does not exist.
        fragment = fm.findFragmentByTag("TOP_WORDS");
        if (fragment != null) {
            topWordsFragment = (TopWordsFragment) fragment;
        } else {
            fm.beginTransaction().add(pager.getId(), topWordsFragment, "TOP_WORDS").commit();
        }

        // Finalize setup with an adapter referencing the fragments and OnPagerChangeListener for
        // listening to pager events.
        Fragment[] fragments = {toDoTasksFragment, finishedTasksFragment, topWordsFragment};
        MainInappViewPagerAdapter adapter = new MainInappViewPagerAdapter(fragments);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new MainInAppViewPagerOnPageChangeListener(this));
    }

    @Override
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        activityInterface.setupBackgroundService(serviceClass, this);
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {
        mainInAppBackgroundTasker = (MainInAppBackgroundTasker) backgroundTasker;
        mainInAppBackgroundTasker.bindClient(this);
        onTodoTasksWindowSelected();
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        mainInAppBackgroundTasker = (MainInAppBackgroundTasker) backgroundTaskManager;
        mainInAppBackgroundTasker.bindClient(this);

        switch (pager.getCurrentItem()) {
            case 0:
                onTodoTasksWindowSelected();
                break;
            case 1:
                onFinishedTasksWindowSelected();
                break;
            case 2:
                onTopWordsWindowSelected();
                break;
        }
    }

    @Override
    public Context getContext() {
        return activityInterface.getContext();
    }

    @Override
    public Window getWindow() {
        return activityInterface.getWindow();
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        activityInterface.runOnUiThread(runnable);
    }

    @Override
    public ToDoTasksFragment getToDoTasksFragment() {
        return toDoTasksFragment;
    }

    @Override
    public FinishedTasksFragment getFinishedTasksFragment() {
        return finishedTasksFragment;
    }

    @Override
    public TopWordsFragment getTopWordsFragment() {
        return topWordsFragment;
    }

    @Override
    public ViewPager getPager() {
        return pager;
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public TextView.OnEditorActionListener getAddTaskOnEditorActionListener() {
        return addTaskOnEditorActionListener;
    }

    @Override
    public TextWatcher getAddTaskBoxTextWatcher() {
        return addTaskBoxTextWatcher;
    }

    @Override
    public AdapterView.OnItemSelectedListener getTopWordsNumResultsSpinnerItemSelectedListener() {
        return topWordsNumResultsSpinnerItemSelectedListener;
    }

    @Override
    public MainInAppRecyclerViewOnScrollChangedListener getRecyclerViewOnScrollListener() {
        return recyclerViewOnScrollListener;
    }

    @Override
    public void onTodoTasksWindowSelected() {
        if (mainInAppBackgroundTasker != null) {
            toDoTasksFragment.showFetchingData();
            mainInAppBackgroundTasker.fetchToDoTasksList();
            setListItemSwipeHandler(new TodoTasksListItemSwipeHandler(this));
        }
    }

    @Override
    public void onFinishedTasksWindowSelected() {
        if (mainInAppBackgroundTasker != null) {
            finishedTasksFragment.showFetchingData();
            mainInAppBackgroundTasker.fetchFinishedTasksList();
        }
    }

    @Override
    public void onTopWordsWindowSelected() {
        if (mainInAppBackgroundTasker != null) {
            topWordsFragment.showFetchingData();
            mainInAppBackgroundTasker.fetchTopWordsList(10);
            setListItemSwipeHandler(new TopWordsListItemSwipeHandler(this));
        }
    }

    @Override
    public void onAddTaskBoxTextChanged(Editable s) {
        // Show the Add Tasks button when user types an input text for the Add Task Box.
        // Otherwise, hide the button when text is cleared.
        if (s.length() > 0) {
            toDoTasksFragment.showAddTaskButton();
        } else {
            toDoTasksFragment.hideAddTaskButton();
        }
    }

    @Override
    public void onAddTaskButtonClicked() {
        // Close soft keyboard
        if (activityInterface.getCurrentFocus() != null) {
            final IBinder token = activityInterface.getCurrentFocus().getWindowToken();
            final InputMethodManager inputMethodManager = (InputMethodManager) activityInterface
                    .getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                activityInterface.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputMethodManager.hideSoftInputFromWindow(token, 0);
                    }
                });
            }
        }

        onAddTaskUserInput();
    }

    @Override
    public void onAddTaskUserInput() {
        // If user inputted a task, trigger the onAddNewTask for handling, passing the string
        // of the new task to add.
        String task = toDoTasksFragment.getAddTaskBoxTextInput();

        // Clear add-task box's text and hide add-task button
        toDoTasksFragment.clearAddTaskBox();
        if (task != null && task.length() > 0) {
            showProgressDialog(R.string.activity_maininapp_addtaskprogress);
            mainInAppBackgroundTasker.submitNewTask(task);
        }
    }

    @Override
    public void showProgressDialog(final int stringResId) {
        activityInterface.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialogTextView.setText(stringResId);
                progressDialog.show();
            }
        });
    }

    @Override
    public void dismissProgressDialog() {
        activityInterface.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialogTextView.setText(null);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void setListItemSwipeHandler(ListItemSwipeHandler listItemSwipeHandler) {
        this.listItemSwipeHandler = listItemSwipeHandler;
    }

    @Override
    public void onListItemTouch(View v, MotionEvent event, int position) {
        //If no list item has been selected yet, select this item.
        if (selectedItemView == null) {
            selectedItemView = v;
            selectedItemPosition = position;
        }

        if (v == selectedItemView) {
            // This allows the selected item to get motion events that would otherwise be stolen by the
            // ViewPager.
            pager.requestDisallowInterceptTouchEvent(true);

            // Stop item when touched
            selectedItemView.animate().cancel();

            listItemSwipeHandler.handleMotionEvent(selectedItemView, event);
        }
    }

    @Override
    public void onListItemSelectionClear() {
        selectedItemView = null;
        selectedItemPosition = -1;
    }

    @Override
    public View getSelectedItemView() {
        return selectedItemView;
    }

    @Override
    public void setSelectedItemView(View itemView) {
        selectedItemView = itemView;
    }

    @Override
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    @Override
    public void onTaskMarkedFinishedAction() {
        mainInAppBackgroundTasker.submitFinishedTask(selectedItemPosition);

        toDoTasksFragment.removeTask(selectedItemPosition);

        // Show a pop-up checkmark.
        float x = selectedItemView.getTranslationX();
        int[] l = new int[2];
        selectedItemView.getLocationOnScreen(l);
        float y = l[1];
        new PopUpCheckMark(getContext()).popUp(x, y);
    }

    @Override
    public void onToDoTaskLongClicked(final float x, final float y, final int
            longClickedListItemPosition) {
        activityInterface.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Cleat selection for swipe.
                onListItemSelectionClear();

                // Create a pop-up window displaying a message bubble with a text that says
                // "Delete".
                int deleteBubbleWidth = getContext().getResources().getDimensionPixelSize(R.dimen
                        .fragment_todotasks_deletebubble_width);
                final PopupWindow deleteBubble = new PopupWindow(deleteBubbleWidth, ViewGroup.LayoutParams
                        .WRAP_CONTENT);
                deleteBubbleView = View.inflate(getContext(), R.layout
                        .fragment_todotasks_deletebubble, null);
                deleteBubble.setContentView(deleteBubbleView);
                deleteBubbleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListItemSelectionClear();
                        deleteBubble.dismiss();

                        showProgressDialog(R.string.activity_maininapp_deletetaskprogress);

                        mainInAppBackgroundTasker.deleteToDoTask(longClickedListItemPosition);
                    }
                });
                // setFocusable and setBackgroundDrawable are necessary to allow dismissing the
                // pop-up window when user touches outside.
                deleteBubble.setFocusable(true);
                deleteBubble.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteBubble.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        // Clear selection for swipe when dismiss allow for any future swipes.
                        onListItemSelectionClear();
                    }
                });

                // Show the pop-up delete bubble right above the point of touch.
                // To center the delete bubble on the point of touch, move it(in other words,
                // move its left property) to the left of the point of touch by half of its width.
                // To place the delete bubble right above the point of touch, move it(in other
                // words, move its top property) above the point of touch by equal to its height
                // plus some extra space.
                View screen = getWindow().getDecorView().getRootView();
                float centerX = x - (float) deleteBubbleWidth / 2;
                float topSpace = getContext().getResources().getDimensionPixelSize(R.dimen
                        .fragment_todotasks_deletebubble_topallowance);
                float topY = y - ((float) deleteBubbleWidth * 28.0f / 85.0f) - topSpace;
                deleteBubble.showAtLocation(screen, Gravity.START | Gravity.TOP, (int) centerX,
                        (int) topY);
            }
        });
    }

    @Override
    public void onWordSwipedToMark() {
        TopWordsFragment.ViewState currentViewState = topWordsFragment.getCurrentViewState();
        switch (currentViewState) {
            case TOP:
                showProgressDialog(R.string.activity_maininapp_excludewordprogress);
                mainInAppBackgroundTasker.setExcluded(selectedItemPosition, 1);
                break;
            case EXCLUDED:
                showProgressDialog(R.string.activity_maininapp_unexcludewordprogress);
                mainInAppBackgroundTasker.setExcluded(selectedItemPosition, 0);
                break;
        }
    }

    @Override
    public void onChangeTopWordsNumResults(int numResults) {
        // When user scrolls from to-do tasks page to finished tasks page, the top words page is
        // created as an off-screen page. This causes the onItemSelected of the spinner to be
        // invoked. Make sure this doesn't result into fetching top words by making sure that
        // fetching of top words is only done when top words page is selected.
        if (pager.getCurrentItem() == 2) {
            if (mainInAppBackgroundTasker != null) {
                topWordsFragment.showFetchingData();
                mainInAppBackgroundTasker.fetchTopWordsList(numResults);
            }
        }
    }

    @Override
    public void onTopWordsViewButtonClicked() {
        topWordsFragment.showFetchingData();
        TopWordsFragment.ViewState viewState = topWordsFragment.switchViewState();

        switch (viewState) {
            case TOP:
                int numResults = topWordsFragment.getNumResults();
                mainInAppBackgroundTasker.fetchTopWordsList(numResults);
                break;

            case EXCLUDED:
                mainInAppBackgroundTasker.fetchExcludedWordsList();
                break;
        }
    }

    @Override
    public void onReloadClicked() {
        int currentItem = pager.getCurrentItem();
        switch (currentItem) {
            case 0:
                onListItemSelectionClear();
                toDoTasksFragment.showFetchingData();
                mainInAppBackgroundTasker.fetchToDoTasksList();
                break;
            case 1:
                finishedTasksFragment.showFetchingData();
                mainInAppBackgroundTasker.fetchFinishedTasksList();
                break;
            case 2:
                onListItemSelectionClear();
                topWordsFragment.showFetchingData();
                TopWordsFragment.ViewState viewState = topWordsFragment.getCurrentViewState();
                switch (viewState) {
                    case TOP:
                        int numResults = topWordsFragment.getNumResults();
                        mainInAppBackgroundTasker.fetchTopWordsList(numResults);
                        break;
                    case EXCLUDED:
                        mainInAppBackgroundTasker.fetchExcludedWordsList();
                        break;
                }
                break;
        }
    }

    @Override
    public void logout() {
        if (mainInAppBackgroundTasker instanceof MainInAppManager) {
            Bundle input = new Bundle();
            input.putInt("action", SplashBackgroundTasker.Action.LOGOUT.ordinal());
            activityInterface.switchScreen(SplashActivity.class, (BackgroundTaskManager)
                    mainInAppBackgroundTasker, input);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
}
