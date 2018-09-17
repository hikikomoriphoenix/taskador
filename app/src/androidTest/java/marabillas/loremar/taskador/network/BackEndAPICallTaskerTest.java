package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.network.resuilthandlers.AddTasksTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.FinishTasksTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetFinishedTasksTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.GetTasksTaskTest;
import marabillas.loremar.taskador.network.resuilthandlers.LoginTest;
import marabillas.loremar.taskador.network.resuilthandlers.SignupTest;
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
    public void addTasks() {
        String username = "test1";
        String token = null;
        try {
            token = getAuthToken(username);
        } catch (AccountUtils.GetAuthTokenException e) {
            Assert.fail(e.getMessage());
        }

        String[] tasks = {"task1", "task2", "task3"};

        AddTasksTaskTest addTasksTaskTest = new AddTasksTaskTest();

        BackEndAPICallTasker.getInstance().addTasks(addTasksTaskTest, username, token, tasks);

        addTasksTaskTest.waitForResults();
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

        GetTasksTaskTest getTasksTaskTest = new GetTasksTaskTest();

        BackEndAPICallTasker.getInstance().getTasks(getTasksTaskTest, username, token);

        getTasksTaskTest.waitForResults();
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
        IdTaskPair entry = new IdTaskPair();
        entry.id = 1;
        entry.task = "task1";
        entries[0] = entry;

        entry = new IdTaskPair();
        entry.id = 2;
        entry.task = "task2";
        entries[1] = entry;

        entry = new IdTaskPair();
        entry.id = 3;
        entry.task = "task3";
        entries[2] = entry;

        FinishTasksTaskTest finishTasksTaskTest = new FinishTasksTaskTest();

        BackEndAPICallTasker.getInstance().finishTasks(finishTasksTaskTest, username, token,
                entries);

        finishTasksTaskTest.waitForResults();
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

        GetFinishedTasksTaskTest getFinishedTasksTaskTest = new GetFinishedTasksTaskTest();

        BackEndAPICallTasker.getInstance().getFinishedTasks(getFinishedTasksTaskTest, username,
                token);

        getFinishedTasksTaskTest.waitForResults();
    }
}