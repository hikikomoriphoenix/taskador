package marabillas.loremar.taskador.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.components.MainInAppEventHandler;
import marabillas.loremar.taskador.ui.adapter.MainInappViewPagerAdapter;
import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;

/**
 * Activity for main in-app screen. This screen allows the user to list to-do tasks, show
 * finished tasks and also features listing of most frequently used words for tasks. These
 * features are contained in a ViewPager allowing the user to swipe between them.
 */
public class MainInAppActivity extends AppCompatActivity implements ViewPager
        .OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, TextView
        .OnEditorActionListener, View.OnClickListener {
    private ToDoTasksFragment toDoTasksFragment;
    private FinishedTasksFragment finishedTasksFragment;
    private TopWordsFragment topWordsFragment;
    private ViewPager pager;
    private MainInAppEventHandler mainInAppEventHandler;
    private View contentView;

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
        pager.addOnPageChangeListener(this);

        contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mainInAppEventHandler.onTodoTasksWindowSelected();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public ToDoTasksFragment getToDoTasksFragment() {
        return toDoTasksFragment;
    }

    public void setMainInAppEventHandler(MainInAppEventHandler mainInAppEventHandler) {
        this.mainInAppEventHandler = mainInAppEventHandler;
        this.mainInAppEventHandler.onMainInAppIsReady(this);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            toDoTasksFragment.clearAddTaskBox();

            triggerOnAddNewTaskEventIfUserInputtedNewTask();
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        // Handle add-task button clicked
        if (v.getId() == R.id.fragment_todotasks_addtask_button) {
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

            triggerOnAddNewTaskEventIfUserInputtedNewTask();
        }
    }

    private void triggerOnAddNewTaskEventIfUserInputtedNewTask() {
        // If user inputted a task, trigger the onAddNewTask for handling, passing the string
        // of the new task to add.
        String task = toDoTasksFragment.getAddTaskBoxTextInput();
        if (task != null && task.length() > 0) {
            mainInAppEventHandler.onAddNewTask(task);
        }
    }
}
