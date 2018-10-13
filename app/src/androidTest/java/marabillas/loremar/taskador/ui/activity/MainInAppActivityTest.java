package marabillas.loremar.taskador.ui.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.background.MainInAppBackgroundTasker;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.entries.WordCountPair;

public class MainInAppActivityTest {
    @Rule
    public ActivityTestRule<MainInAppActivity> activityTestRule = new ActivityTestRule<>
            (MainInAppActivity.class);

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Test
    public void test() {
        final MainInAppActivity mainInAppActivity = activityTestRule.getActivity();

        // Prepare the list of tasks
        final List<String> todoTasks = new ArrayList<>();
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
        Collections.addAll(todoTasks, tasksArray);

        final List<TaskDatePair> finishedTasks = new ArrayList<>();

        for (int i = 0; i < 30; ++i) {
            finishedTasks.add(new TaskDatePair("A finished task", "OCT 1, 2018"));
        }

        // Prepare top words
        final List<WordCountPair> topWords = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            topWords.add(new WordCountPair("word" + i, String.valueOf(100 - i * 2)));
        }

        // Prepare excluded words
        final List<String> excludedWords = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            excludedWords.add("excludedWord" + i);
        }

        class MainInAppBackgroundTaskerTest implements MainInAppBackgroundTasker {
            private MainInAppActivity activity;

            @Override
            public void fetchToDoTasksList() {
                activity.getToDoTasksFragment().updateList(todoTasks);
            }

            @Override
            public void submitNewTask(String task) {

            }

            @Override
            public void fetchFinishedTasksList() {
                activity.getFinishedTasksFragment().updateList(finishedTasks);
            }

            @Override
            public void fetchTopWordsList(int numResults) {
                switch (numResults) {
                    case 10:
                        List<WordCountPair> top10 = topWords.subList(0, 10);
                        activity.getTopWordsFragment().updateTopWordsList(top10);
                        break;
                    case 20:
                        activity.getTopWordsFragment().updateTopWordsList(topWords);
                }
            }

            @Override
            public void fetchExcludedWordsList() {
                activity.getTopWordsFragment().updateExcludedWordsList(excludedWords);
            }

            @Override
            public void bindActivity(MainInAppActivity activity) {
                this.activity = activity;
            }

            @Override
            public MainInAppActivity getActivity() {
                return activity;
            }
        }

        mainInAppActivity.setBackgroundTasker(new MainInAppBackgroundTaskerTest());

/*        BackgroundServiceConnection conn = new BackgroundServiceConnection(mainInAppActivity);
        Intent intent = new Intent(mainInAppActivity, MainInAppManager.class);
        try {
            serviceTestRule.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (TimeoutException e) {
            Assert.fail(e.getMessage());
        }*/

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
