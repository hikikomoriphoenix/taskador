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
import marabillas.loremar.taskador.background.ActivityBinder;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.MainInAppBackgroundTasker;
import marabillas.loremar.taskador.ui.adapter.MainInappViewPagerAdapter;
import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;
import marabillas.loremar.taskador.ui.listeners.AddTaskBoxTextWatcher;
import marabillas.loremar.taskador.ui.listeners.AddTaskOnEditorActionListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppOnClickListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppOnTouchListener;
import marabillas.loremar.taskador.ui.listeners.MainInAppViewPagerOnPageChangeListener;
import marabillas.loremar.taskador.ui.listeners.TopWordsNumResultsSpinnerItemSelectedListener;
import marabillas.loremar.taskador.ui.motion.ListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TodoTasksListItemSwipeHandler;
import marabillas.loremar.taskador.ui.motion.TopWordsListItemSwipeHandler;

/**
 * Activity for main in-app screen. This screen allows the user to list to-do tasks, show
 * finished tasks and also features listing of most frequently used words for tasks. These
 * features are contained in a ViewPager allowing the user to swipe between them.
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
        pager.setOnTouchListener(new MainInAppOnTouchListener(this));

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
    public void setBackgroundTasker(ActivityBinder activityBinder) {
        mainInAppBackgroundTasker = (MainInAppBackgroundTasker) activityBinder;
        mainInAppBackgroundTasker.bindActivity(this);
        onTodoTasksWindowSelected();
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        mainInAppBackgroundTasker = (MainInAppBackgroundTasker) backgroundTaskManager;
        mainInAppBackgroundTasker.bindActivity(this);
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

    public void onTodoTasksWindowSelected() {
        mainInAppBackgroundTasker.fetchToDoTasksList();
        setListItemSwipeHandler(new TodoTasksListItemSwipeHandler(this));
    }

    public void onFinishedTasksWindowSelected() {
        mainInAppBackgroundTasker.fetchFinishedTasksList();
    }

    public void onTopWordsWindowSelected() {
        mainInAppBackgroundTasker.fetchTopWordsList(10);
        setListItemSwipeHandler(new TopWordsListItemSwipeHandler(this));
    }

    public void onAddTaskBoxTextChanged(Editable s) {
        if (s.length() > 0) {
            toDoTasksFragment.showAddTaskButton();
        } else {
            toDoTasksFragment.hideAddTaskButton();
        }
    }

    public void onAddTaskButtonClicked() {
        // Close soft keyboard
        if (getCurrentFocus() != null) {
            IBinder token = getCurrentFocus().getWindowToken();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(token, 0);
            }
        }

        // Clear add-task box's text and hide add-task button
        toDoTasksFragment.clearAddTaskBox();

        onAddTaskUserInput();
    }

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

    public ListItemSwipeHandler getListItemSwipeHandler() {
        return listItemSwipeHandler;
    }

    public void onListItemTouch(View v, MotionEvent event, int position) {
        selectedItemView = v;
        selectedItemPosition = position;

        listItemSwipeHandler.handleMotionEvent(v, event);
    }

    public void onListItemRelease() {
        selectedItemPosition = -1;
        selectedItemView = null;
    }

    public View getSelectedItemView() {
        return selectedItemView;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void onMarkTaskChecked() {
        // TODO Update item in the data. Mark it as checked. Notify recyclerview adapter to update
        // its view.
    }

    public void onWordSwipedToMark() {
        // TODO implement
    }

    public void onChangeTopWordsNumResults(int numResults) {
        mainInAppBackgroundTasker.fetchTopWordsList(numResults);
    }
}
