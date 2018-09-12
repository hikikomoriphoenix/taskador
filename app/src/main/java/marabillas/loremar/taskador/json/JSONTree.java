package marabillas.loremar.taskador.json;

/**
 * This class allows to easily construct a JSON structured data.
 * <p>
 * Example:
 * <pre>{@code
 * String data = new JSONTree()
 *      .put("username", "JohnDoe")
 *      .put("token", "12cbi23pn33p12")
 *      .put("tasks", taskArray)
 *      .put("sub_data", new JSONTree().put("date", today))
 *      .toString();
 * }</pre>
 */
public class JSONTree {
    public JSONTree() {
    }

    public JSONTree put(String key, boolean value) {
        return this;
    }

    public JSONTree put(String key, double value) {
        return this;
    }

    public JSONTree put(String key, int value) {
        return this;
    }

    public JSONTree put(String key, long value) {
        return this;
    }

    public JSONTree put(String key, String value) {
        return this;
    }

    public JSONTree put(String key, JSONTree value) {
        return this;
    }

    public JSONTree put(String key, boolean[] value) {
        return this;
    }

    public JSONTree put(String key, double[] value) {
        return this;
    }

    public JSONTree put(String key, int[] value) {
        return this;
    }

    public JSONTree put(String key, long[] value) {
        return this;
    }

    public JSONTree put(String key, String[] value) {
        return this;
    }

    public JSONTree put(String key, JSONTree[] value) {
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
