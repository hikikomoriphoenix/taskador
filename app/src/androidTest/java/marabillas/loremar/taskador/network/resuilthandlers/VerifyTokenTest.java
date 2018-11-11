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

import marabillas.loremar.taskador.network.tasks.VerifyTokenTask;

public class VerifyTokenTest extends ResultHandlerTest implements VerifyTokenTask.ResultHandler {
    @Override
    public void tokenVerifiedCorrect(String message) {
        handleSuccess(message);
    }

    @Override
    public void failedToRequestVerification(String message) {
        handleFailure(message);
    }

    @Override
    public void tokenVerifiedNotCorrect(String message) {
        handleFailure(message);
    }

    @Override
    public void tokenVerificationIncomplete(String message) {
        handleFailure(message);
    }

    @Override
    public void backEndFailedToVerifyToken(String message) {
        handleFailure(message);
    }
}
