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

package marabillas.loremar.taskador.network.resuilthandlers;

import junit.framework.Assert;

import marabillas.loremar.taskador.json.FailedToGetFieldException;
import marabillas.loremar.taskador.json.JSON;
import marabillas.loremar.taskador.json.JSON_Array;
import marabillas.loremar.taskador.network.tasks.GetFinishedTasksTask;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetFinishedTasksTest extends ResultHandlerTest implements GetFinishedTasksTask.ResultHandler {
    @Override
    public void finishedTasksObtained(String message, JSON data) {
        try {
            JSON_Array tasks = data.getArray("tasks");
            for (int i = 0; i < tasks.getCount(); ++i) {
                JSON item = tasks.getObject(i);
                String dateFinished = item.getString("date_finished");
                log("dateFinished: " + dateFinished);
                String task = item.getString("task");
                assertThat(task, is("tasks" + (tasks.getCount() - 1 - i)));
            }
        } catch (FailedToGetFieldException e) {
            Assert.fail(e.getMessage());
        }

        handleSuccess(message);
    }

    @Override
    public void failedGetFinishedTasksRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToGiveFinishedTasks(String message) {
        handleFailure(message);
    }

    @Override
    public void getFinishedTasksIncomplete(String message) {
        handleFailure(message);
    }
}
