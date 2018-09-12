package marabillas.loremar.taskador.json;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JSONTreeTest {
    @Test
    public void test() {
        String data = null;
        try {
            data = new JSONTree()
                    .put("booleanVar", true)
                    .put("doubleVar", 123.456)
                    .put("intVar", 12345)
                    .put("longVar", 1234567890L)
                    .put("stringVar", "Hello World")
                    .put("objectVar", new JSONTree().put("stringVar", "Hello World"))
                    .toString();
        } catch (JSONTreeException e) {
            Assert.fail("Exception on on constructing JSON data: " + e.getMessage());
        }
        String expectedData = "{\"booleanVar\":true,\"doubleVar\":123.456," +
                "\"intVar\":12345,\"longVar\":1234567890,\"stringVar\":\"Hello World\"," +
                "\"objectVar\":{\"stringVar\":\"Hello World\"}}";
        assertThat(data, is(expectedData));

        boolean[] booleans = {true, false, true};
        double[] doubles = {1.2, 3.4, 5.6};
        int[] ints = {123, 456, 789};
        long[] longs = {1234567890L, 9876543210L};
        String[] strings = {"red", "blue", "yellow", "green"};
        JSONTree[] jsons = {new JSONTree(), new JSONTree()};

        data = null;
        try {
            data = new JSONTree()
                    .put("booleans", booleans)
                    .put("doubles", doubles)
                    .put("ints", ints)
                    .put("longs", longs)
                    .put("strings", strings)
                    .put("jsons", jsons)
                    .toString();
        } catch (JSONTreeException e) {
            Assert.fail("Exception on on constructing JSON data: " + e.getMessage());
        }

        expectedData = "{\"booleans\":[true,false,true],\"doubles\":[1.2,3.4,5.6]," +
                "\"ints\":[123,456,789],\"longs\":[1234567890,9876543210],\"strings\":[\"red\"," +
                "\"blue\",\"yellow\",\"green\"],\"jsons\":[{},{}]}";
        assertThat(data, is(expectedData));
    }
}
