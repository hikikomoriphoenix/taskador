package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.network.resuilthandlers.AddTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.CheckUsernameAvailabilityTest;
import marabillas.loremar.taskador.network.resuilthandlers.DeleteTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.FinishTasksTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetExcludedWordsTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetFinishedTasksTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetTasksTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetTopWordsTest;
import marabillas.loremar.taskador.network.resuilthandlers.LoginTest;
import marabillas.loremar.taskador.network.resuilthandlers.SetExcludedTest;
import marabillas.loremar.taskador.network.resuilthandlers.SignupTest;
import marabillas.loremar.taskador.network.resuilthandlers.UpdateTaskWordsTest;
import marabillas.loremar.taskador.network.resuilthandlers.VerifyTokenTest;
import marabillas.loremar.taskador.utils.AccountUtils;

import static marabillas.loremar.taskador.utils.AccountUtils.getAuthToken;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BackEndAPICallTaskerTest {
    @Test
    public void signup() {
        final String username = "test3";
        final String password = "password";
        SignupTest signupTest = new SignupTest();

        BackEndAPICallTasker.getInstance().signup(signupTest, username, password);

        signupTest.waitForResults();
    }

    @Test
    public void login() {
        final String username = "test1";
        final String password = "password";
        LoginTest loginTest = new LoginTest();

        BackEndAPICallTasker.getInstance().login(loginTest, username, password);

        loginTest.waitForResults();
    }

    @Test
    public void verifyToken() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        VerifyTokenTest verifyTokenTest = new VerifyTokenTest();

        BackEndAPICallTasker.getInstance().verifyToken(verifyTokenTest, username, token);

        verifyTokenTest.waitForResults();
    }

    @Test
    public void addTask() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        String task = "task1";

        AddTaskTest addTaskTest = new AddTaskTest();

        BackEndAPICallTasker.getInstance().addTask(addTaskTest, username, token, task);

        addTaskTest.waitForResults();
    }

    @Test
    public void deleteTask() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        int id = 28;

        DeleteTaskTest deleteTaskTest = new DeleteTaskTest();

        BackEndAPICallTasker.getInstance().deleteTask(deleteTaskTest, username, token, id);

        deleteTaskTest.waitForResults();
    }

    @Test
    public void getTasks() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        GetTasksTest getTasksTest = new GetTasksTest();

        BackEndAPICallTasker.getInstance().getTasks(getTasksTest, username, token);

        getTasksTest.waitForResults();
    }

    @Test
    public void finishTasks() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        IdTaskPair[] entries = new IdTaskPair[3];
        IdTaskPair entry = new IdTaskPair(1, "task1");
        entries[0] = entry;

        entry = new IdTaskPair(2, "task2");
        entries[1] = entry;

        entry = new IdTaskPair(3, "task3");
        entries[2] = entry;

        FinishTasksTest finishTasksTest = new FinishTasksTest();

        BackEndAPICallTasker.getInstance().finishTasks(finishTasksTest, username, token,
                entries);

        finishTasksTest.waitForResults();
    }

    @Test
    public void getFinishedTasks() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        GetFinishedTasksTest getFinishedTasksTest = new GetFinishedTasksTest();

        BackEndAPICallTasker.getInstance().getFinishedTasks(getFinishedTasksTest, username,
                token);

        getFinishedTasksTest.waitForResults();
    }

    @Test
    public void updateTaskWords() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        UpdateTaskWordsTest updateTaskWordsTest = new UpdateTaskWordsTest();

        BackEndAPICallTasker.getInstance().updateTaskWords(updateTaskWordsTest, username,
                token);

        updateTaskWordsTest.waitForResults();
    }

    @Test
    public void getTopWords() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        GetTopWordsTest getTopWordsTest = new GetTopWordsTest();

        BackEndAPICallTasker.getInstance().getTopWords(getTopWordsTest, username, token, 10);

        getTopWordsTest.waitForResults();
    }

    @Test
    public void setExcluded() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        String word = "a";
        //int excluded = 0;
        int excluded = 1;

        SetExcludedTest setExcludedTest = new SetExcludedTest();

        BackEndAPICallTasker.getInstance().setExcluded(setExcludedTest, username, token, word,
                excluded);

        setExcludedTest.waitForResults();
    }

    @Test
    public void getExcludedWords() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        GetExcludedWordsTest getExcludedWordsTest = new GetExcludedWordsTest();

        BackEndAPICallTasker.getInstance().getExcludedWords(getExcludedWordsTest, username, token);

        getExcludedWordsTest.waitForResults();
    }

    @Test
    public void checkUsernameAvailability() {
        String username = "test1";

        CheckUsernameAvailabilityTest checkUsernameAvailabilityTest = new
                CheckUsernameAvailabilityTest();

        BackEndAPICallTasker.getInstance().checkUsernameAvailability
                (checkUsernameAvailabilityTest, username);

        checkUsernameAvailabilityTest.waitForResults();
    }
}