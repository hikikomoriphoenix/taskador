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
import marabillas.loremar.taskador.network.tasks.GetTopWordsTask;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetTopWordsTest extends ResultHandlerTest implements GetTopWordsTask.ResultHandler {
    @Override
    public void topWordsObtained(String message, JSON data) {
        String[] expectedWords = {"watch", "practice", "clean"};
        int[] expectedCounts = {30, 20, 10};
        try {
            JSON_Array words = data.getArray("top_words");
            for (int i = 0; i < words.getCount(); ++i) {
                JSON item = words.getObject(i);
                String word = item.getString("word");
                int count = item.getInt("count");
                assertThat(word, is(expectedWords[i]));
                assertThat(count, is(expectedCounts[i]));
            }
        } catch (FailedToGetFieldException e) {
            Assert.fail(e.getMessage());
        }

        handleSuccess(message);
    }

    @Override
    public void getTopWordsTaskFailedToPrepareJSONData(String message) {
        handleFailure(message);
    }

    @Override
    public void failedGetTopWordsRequest(String message) {
        handleFailure(message);
    }

    @Override
    public void backendUnableToGetTopWords(String message) {
        handleFailure(message);
    }

    @Override
    public void getTopWordsTaskIncomplete(String message) {
        handleFailure(message);
    }
}
