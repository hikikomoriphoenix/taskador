package marabillas.loremar.taskador.network.resuilthandlers;

import junit.framework.Assert;

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSON_Array;
import marabillas.loremar.taskador.network.tasks.GetTasksTask;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetTasksTaskTest extends ResultHandlerTest implements GetTasksTask.ResultHandler {
    @Override
    public void tasksObtained(String message, JSON data) {
        try {
            JSON_Array tasks = data.getArray("tasks");
            for (int i = 0; i < tasks.getCount(); ++i) {
                JSON item = tasks.getObject(i);
                String task = item.getString("task");
                assertThat(task, is("task" + (i + 1)));
            }
        } catch (FailedToGetFieldException e) {
            Assert.fail(e.getMessage());
        }

        handleSuccess(message);
    }

    @Override
    public void failedGetTasksRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToGiveTasks(String message) {
        handleFailure(message);
    }

    @Override
    public void getTasksTaskIncomplete(String message) {
        handleFailure(message);
    }
}
