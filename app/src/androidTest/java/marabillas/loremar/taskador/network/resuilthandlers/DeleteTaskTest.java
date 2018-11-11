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

import marabillas.loremar.taskador.network.tasks.DeleteTaskTask;

public class DeleteTaskTest extends ResultHandlerTest implements DeleteTaskTask.ResultHandler {
    @Override
    public void taskDeletedSuccessfully(String message) {
        handleSuccess(message);
    }

    @Override
    public void deleteTaskTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedDeleteTaskRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToDeleteTask(String message) {
        handleFailure(message);
    }

    @Override
    public void deleteTaskTaskIncomplete(String message) {
        handleFailure(message);
    }
}
