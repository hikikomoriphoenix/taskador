package marabillas.loremar.taskador.ui.activity;

import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.components.MainInAppEventHandler;

public class MainInAppActivityTest {
    @Rule
    public ActivityTestRule<MainInAppActivity> activityTestRule = new ActivityTestRule<>
            (MainInAppActivity.class);

    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testTodoTasks() {
        final MainInAppActivity mainInAppActivity = activityTestRule.getActivity();

        // Prepare the list of tasks
        final List<String> tasks = new ArrayList<>();
        String[] tasksArray = {
                "A task",
                "A loooooooooooooonnngggggggggggggg task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task",
                "Another task"
        };
        Collections.addAll(tasks, tasksArray);

        MainInAppEventHandler mainInAppEventHandler = new MainInAppEventHandler() {
            @Override
            public void onMainInAppIsReady(MainInAppActivity mainInAppActivity) {
                setupTodoTasksWindow();
            }

            @Override
            public void onTodoTasksWindowSelected() {
                setupTodoTasksWindow();
            }

            @Override
            public void onAddNewTask(String task) {

            }

            private void setupTodoTasksWindow() {
                mainInAppActivity.getToDoTasksFragment().updateList(tasks);
            }
        };
        mainInAppActivity.setMainInAppEventHandler(mainInAppEventHandler);

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
