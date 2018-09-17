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
    public void failedSetExcludedRequest(String message) {
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
