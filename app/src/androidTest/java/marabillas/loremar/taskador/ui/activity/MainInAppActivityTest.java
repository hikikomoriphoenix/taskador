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

package marabillas.loremar.taskador.ui.activity;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.background.MainInAppBackgroundTasker;
import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.entries.WordCountPair;
import marabillas.loremar.taskador.ui.MainInApp;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;

import static marabillas.loremar.taskador.utils.AccountUtils.setCurrentAccountUsername;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainInAppActivityTest {
    @Rule
    public ActivityTestRule<MainInAppActivity> activityTestRule = new ActivityTestRule<>
            (MainInAppActivity.class, true, false);

    @Test
    public void test() {
        activityTestRule.launchActivity(new Intent(App.getInstance(), MainInAppActivity.class));
        final MainInAppActivity mainInAppActivity = activityTestRule.getActivity();

        // Prepare the list of tasks
        final List<IdTaskPair> todoTasks = new ArrayList<>();
        todoTasks.add(new IdTaskPair(1, "A task"));
        todoTasks.add(new IdTaskPair(2, "A looooooooooooooooooonnnnnnnnnnggggggggg task"));
        for (int i = 3; i < 15; ++i) {
            todoTasks.add(new IdTaskPair(3, "task" + i));
        }

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

        MainInApp mainInApp = new MainInApp(mainInAppActivity);

        class MainInAppBackgroundTaskerTest implements MainInAppBackgroundTasker {
            private MainInApp mainInApp;

            @Override
            public void fetchToDoTasksList() {
                mainInApp.getToDoTasksFragment().showRecyclerView();
                mainInApp.getToDoTasksFragment().bindList(todoTasks);
            }

            @Override
            public void submitNewTask(String task) {
                mainInApp.dismissProgressDialog();
                todoTasks.add(new IdTaskPair(0, task));
                mainInApp.getToDoTasksFragment().notifyTaskAdded(todoTasks.size() - 1);
            }

            @Override
            public void deleteToDoTask(int position) {
                mainInApp.dismissProgressDialog();
                mainInApp.getToDoTasksFragment().removeTask(position);
            }

            @Override
            public void submitFinishedTask(int position) {

            }

            @Override
            public void fetchFinishedTasksList() {
                mainInApp.getFinishedTasksFragment().showRecyclerView();
                mainInApp.getFinishedTasksFragment().bindList(finishedTasks);
            }

            @Override
            public void fetchTopWordsList(int numResults) {
                switch (numResults) {
                    case 10:
                        mainInApp.getTopWordsFragment().showRecyclerView();
                        List<WordCountPair> top10 = topWords.subList(0, 10);
                        mainInApp.getTopWordsFragment().bindTopWordsList(top10);
                        break;
                    case 20:
                        mainInApp.getTopWordsFragment().showRecyclerView();
                        mainInApp.getTopWordsFragment().bindTopWordsList(topWords);
                        break;
                }
            }

            @Override
            public void fetchExcludedWordsList() {
                mainInApp.getTopWordsFragment().showRecyclerView();
                mainInApp.getTopWordsFragment().bindExcludedWordsList(excludedWords);
            }

            @Override
            public void setExcluded(int selectedItemPosition, int excluded) {
                mainInApp.dismissProgressDialog();
                TopWordsFragment.ViewState viewState = mainInApp.getTopWordsFragment()
                        .getCurrentViewState();
                switch (viewState) {
                    case TOP:
                        mainInApp.getTopWordsFragment().removeTopWord(selectedItemPosition);
                        break;
                    case EXCLUDED:
                        mainInApp.getTopWordsFragment().removeExcludedWord(selectedItemPosition);
                        break;
                }
            }

            @Override
            public void bindClient(MainInApp client) {
                this.mainInApp = client;
            }

            @Override
            public MainInApp getClient() {
                return mainInApp;
            }
        }

        mainInApp.setBackgroundTasker(new MainInAppBackgroundTaskerTest());

        await();
    }

    @Test
    public void testWithMainInAppManager() {
        App.getInstance().setBackgroundTaskManagerSupport(true);

        setCurrentAccountUsername("test1");

        activityTestRule.launchActivity(new Intent(App.getInstance(), MainInAppActivity.class));

        await();
    }

    private void await() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            org.junit.Assert.fail(e.getMessage());
        }
    }
}
