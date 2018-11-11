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
import marabillas.loremar.taskador.network.tasks.GetExcludedWordsTask;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetExcludedWordsTest extends ResultHandlerTest implements GetExcludedWordsTask.ResultHandler {
    @Override
    public void excludedWordsObtained(String message, JSON data) {
        String[] expectedWords = {"a", "task1", "task2", "task3"};

        try {
            JSON_Array words = data.getArray("words");
            for (int i = 0; i < words.getCount(); ++i) {
                String word = words.getString(i);
                assertThat(word, is(expectedWords[i]));
            }
        } catch (FailedToGetFieldException e) {
            Assert.fail(e.getMessage());
        }

        handleSuccess(message);
    }

    @Override
    public void failedGetExcludedRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToGiveExcludedWords(String message) {
        handleFailure(message);
    }

    @Override
    public void getExcludedWordsTaskIncomplete(String message) {
        handleFailure(message);
    }
}
