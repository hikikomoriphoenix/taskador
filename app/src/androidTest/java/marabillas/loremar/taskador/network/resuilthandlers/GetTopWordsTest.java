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
