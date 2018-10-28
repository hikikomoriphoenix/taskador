package marabillas.loremar.taskador.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.MainInAppBackgroundTasker;
import marabillas.loremar.taskador.background.MainInAppManager;
import marabillas.loremar.taskador.background.SplashBackgroundTasker;
import marabillas.loremar.taskador.ui.adapter.MainInappViewPagerAdapter;
import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;
import marabillas.loremar.taskador.ui.listeners.AddTaskBoxTextWatcher;
import marabillas.loremar.taskador.ui.listeners.AddTaskOnEditorActionListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppOnClickListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppViewPagerOnPageChangeListener;
import marabillas.loremar.taskador.ui.listeners.TopWordsNumResultsSpinnerItemSelectedListener;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TodoTasksListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TopWordsListItemSwipeHandler;

/**
 * Activity for main in-app screen. This screen allows the user to list to-do tasks, show
 * finished tasks and also features listing of most frequently used words for tasks. These
 * features are contained in a {@link ViewPager} allowing the user to swipe between them.
 *
 * MainInAppActivity takes the following responsibilities:
 * 1. Directly handles user interactions in the in-app screen
 * 2. Calls for background tasks to be performed as required
 * 3. Updates the views of components in the in-app screen
 */
public class MainInAppActivity extends BaseAppCompatActivity implements ViewTreeObserver
        .OnGlobalLayoutListener {
    private ToDoTasksFragment toDoTasksFragment;
    private FinishedTasksFragment finishedTasksFragment;
    private TopWordsFragment topWordsFragment;
    private ViewPager pager;
    private View contentView;
    private MainInAppBackgroundTasker mainInAppBackgroundTasker;
    private ListItemSwipeHandler listItemSwipeHandler;

    // Listeners
    private View.OnClickListener onClickListener;
    private TextView.OnEditorActionListener addTaskOnEditorActionListener;
    private TextWatcher addTaskBoxTextWatcher;
    private AdapterView.OnItemSelectedListener topWordsNumResultsSpinnerItemSelectedListener;

    private View selectedItemView;
    private int selectedItemPosition;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininapp);

        toDoTasksFragment = new ToDoTasksFragment();
        finishedTasksFragment = new FinishedTasksFragment();
        topWordsFragment = new TopWordsFragment();

        // Set up ViewPager
        pager = findViewById(R.id.activity_maininapp_viewpager);
        FragmentManager fm = getSupportFragmentManager();
        Fragment[] fragments = {toDoTasksFragment, finishedTasksFragment, topWordsFragment};
        MainInappViewPagerAdapter adapter = new MainInappViewPagerAdapter(fm, fragments);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new MainInAppViewPagerOnPageChangeListener(this));

        contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        // Initialize listeners
        onClickListener = new MainInAppOnClickListener(this);
        addTaskOnEditorActionListener = new AddTaskOnEditorActionListener(this);
        addTaskBoxTextWatcher = new AddTaskBoxTextWatcher(this);
        topWordsNumResultsSpinnerItemSelectedListener = new
                TopWordsNumResultsSpinnerItemSelectedListener(this);

        selectedItemView = null;
        selectedItemPosition = -1;
    }

    @Override
    public void onSetupBackgroundService() {
        pager.setCurrentItem(0);
        setupBackgroundService(MainInAppManager.class);
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
        onTodoTasksWindowSelected();
    }

    public ToDoTasksFragment getToDoTasksFragment() {
        return toDoTasksFragment;
    }

    public FinishedTasksFragment getFinishedTasksFragment() {
        return finishedTasksFragment;
    }

    public TopWordsFragment getTopWordsFragment() {
        return topWordsFragment;
    }

    public ViewPager getPager() {
        return pager;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public TextView.OnEditorActionListener getAddTaskOnEditorActionListener() {
        return addTaskOnEditorActionListener;
    }

    public TextWatcher getAddTaskBoxTextWatcher() {
        return addTaskBoxTextWatcher;
    }

    public AdapterView.OnItemSelectedListener getTopWordsNumResultsSpinnerItemSelectedListener() {
        return topWordsNumResultsSpinnerItemSelectedListener;
    }

    @Override
    public void onGlobalLayout() {
        // Try to remove focus from EditText when soft keyboard is closed.
        // When soft keyboard closes the height of visible portion of the screen becomes equal to
        // the height of the total screen. In other words, the screen becomes completely visible.
        Rect visibleRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(visibleRect);
        int totalHeight = contentView.getRootView().getHeight();
        int visibleHeight = visibleRect.bottom;
        if (visibleHeight == totalHeight) {
            if (getCurrentFocus() != null) {
                getCurrentFocus().clearFocus();
            }
        }
    }

    /**
     * Invoked when {@link ToDoTasksFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    public void onTodoTasksWindowSelected() {
        mainInAppBackgroundTasker.fetchToDoTasksList();
        setListItemSwipeHandler(new TodoTasksListItemSwipeHandler(this));
    }

    /**
     * Invoked when {@link FinishedTasksFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    public void onFinishedTasksWindowSelected() {
        mainInAppBackgroundTasker.fetchFinishedTasksList();
    }

    /**
     * Invoked when {@link TopWordsFragment} becomes the current selected item in the in-app
     * screen's {@link ViewPager}.
     */
    public void onTopWordsWindowSelected() {
        mainInAppBackgroundTasker.fetchTopWordsList(10);
        setListItemSwipeHandler(new TopWordsListItemSwipeHandler(this));
    }

    /**
     * Invoked when the text in the Add Task Box which is {@link ToDoTasksFragment}'s
     * {@link android.widget.EditText} for adding new tasks, is changed due to user typing on the
     * soft keyboard or text being programmatically cleared.
     *
     * @param s The new text in the Add Taskbox
     */
    public void onAddTaskBoxTextChanged(Editable s) {
        // Show the Add Tasks button when user types an input text for the Add Task Box.
        // Otherwise, hide the button when text is cleared.
        if (s.length() > 0) {
            toDoTasksFragment.showAddTaskButton();
        } else {
            toDoTasksFragment.hideAddTaskButton();
        }
    }

    /**
     * Invoked when the Add Task Button, which is {@link ToDoTasksFragment}'s
     * {@link android.widget.ImageButton} for submitting new task to user's account, is clicked.
     */
    public void onAddTaskButtonClicked() {
        // Close soft keyboard
        if (getCurrentFocus() != null) {
            final IBinder token = getCurrentFocus().getWindowToken();
            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputMethodManager.hideSoftInputFromWindow(token, 0);
                    }
                });
            }
        }

        // Clear add-task box's text and hide add-task button
        toDoTasksFragment.clearAddTaskBox();

        onAddTaskUserInput();
    }

    /**
     * Invoked when a new task that was inputted by the user needs to be submitted to user's
     * account.
     */
    public void onAddTaskUserInput() {
        // Clear add-task box's text and hide add-task button
        toDoTasksFragment.clearAddTaskBox();

        // If user inputted a task, trigger the onAddNewTask for handling, passing the string
        // of the new task to add.
        String task = toDoTasksFragment.getAddTaskBoxTextInput();
        if (task != null && task.length() > 0) {
            mainInAppBackgroundTasker.submitNewTask(task);
        }
    }

    public void setListItemSwipeHandler(ListItemSwipeHandler listItemSwipeHandler) {
        this.listItemSwipeHandler = listItemSwipeHandler;
    }

    /**
     * Invoked when a list item in {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} is touched.
     *
     * @param v        list item's view
     * @param event    touch event of the list item's view
     * @param position position of the item in the list
     */
    public void onListItemTouch(View v, MotionEvent event, int position) {
        //If no list item has been selected yet, select this item.
        if (selectedItemView == null) {
            selectedItemView = v;
            selectedItemPosition = position;
        }

        // This allows the selected item to get motion events that would otherwise be stolen by the
        // ViewPager.
        pager.requestDisallowInterceptTouchEvent(true);

        // Stop item when touched
        selectedItemView.animate().cancel();

        listItemSwipeHandler.handleMotionEvent(v, event);
    }

    /**
     * Clear selection of list item.
     */
    public void onListItemClear() {
        selectedItemPosition = -1;
        selectedItemView = null;
    }

    /**
     * Gets the view of the item in {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} being selected.
     *
     * @return a view
     */
    public View getSelectedItemView() {
        return selectedItemView;
    }

    /**
     * Gets the position of the item in the list corresponding to the item in
     * {@link ToDoTasksFragment}'s or {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} being selected.
     *
     * @return an integer
     */
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    /**
     * This method is invoked when an item in {@link ToDoTasksFragment}'s
     * {@link android.support.v7.widget.RecyclerView} is swiped to enough distance to mark it as
     * checked. Upon release, the selected item that is checked will be submitted as a finished
     * task.
     */
    public void onMarkTaskChecked() {
        // TODO Update item in the data. Mark it as checked. Notify recyclerview adapter to update
        // its view.
    }

    /**
     * This method is invoked when an item in {@link TopWordsFragment}'s
     * {@link android.support.v7.widget.RecyclerView} is swiped to enough distance to submit the
     * selected item to the back-end server to set it as excluded from the top words.
     */
    public void onWordSwipedToMark() {
        // TODO implement
    }

    /**
     * This method is invoked when the user selects, via the {@link android.widget.Spinner} in
     * {@link TopWordsFragment}, a different number of results for top words.
     *
     * @param numResults desired number of results for top words
     */
    public void onChangeTopWordsNumResults(int numResults) {
        mainInAppBackgroundTasker.fetchTopWordsList(numResults);
    }

    /**
     * This method is invoked when the user presses the {@link android.widget.Button} labeled
     * 'VIEW EXCLUDED' or 'VIEW TOP' in {@link TopWordsFragment}. This allows the user to choose
     * which, between top words list and excluded words list, to view.
     */
    public void onTopWordsViewButtonClicked() {
        TopWordsFragment.ViewState viewState = topWordsFragment.switchViewState();

        switch (viewState) {
            case TOP:
                mainInAppBackgroundTasker.fetchTopWordsList(10);
                break;

            case EXCLUDED:
                mainInAppBackgroundTasker.fetchExcludedWordsList();
                break;
        }
    }

    /**
     * Log out from account. Set current account will be cleared and you will be directed to login
     * screen.
     */
    public void logout() {
        if (mainInAppBackgroundTasker instanceof MainInAppManager) {
            Bundle input = new Bundle();
            input.putInt("action", SplashBackgroundTasker.Action.LOGOUT.ordinal());
            switchScreen(SplashActivity.class, (BackgroundTaskManager) mainInAppBackgroundTasker, input);
        }
    }
}