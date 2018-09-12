package marabillas.loremar.taskador.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private JSONObject tree;

    public JSONTree() {
        tree = new JSONObject();
    }

    public JSONTree put(String key, boolean value) throws JSONTreeException {
        try {
            tree.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, double value) throws JSONTreeException {
        try {
            tree.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, int value) throws JSONTreeException {
        try {
            tree.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, long value) throws JSONTreeException {
        try {
            tree.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, String value) throws JSONTreeException {
        try {
            tree.put(key, value);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, JSONTree value) throws JSONTreeException {
        String jsonString = value.toString();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            tree.put(key, jsonObject);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, boolean[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (boolean item :
                    value) {
                array.put(item);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, double[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (double item :
                    value) {
                array.put(item);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, int[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (int item :
                    value) {
                array.put(item);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, long[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (long item :
                    value) {
                array.put(item);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, String[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (String item :
                    value) {
                array.put(item);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    public JSONTree put(String key, JSONTree[] value) throws JSONTreeException {
        JSONArray array = new JSONArray();
        try {
            for (JSONTree item :
                    value) {
                String jsonString = item.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                array.put(jsonObject);
            }
            tree.put(key, array);
            return this;
        } catch (JSONException e) {
            throw new JSONTreeException(e);
        }
    }

    /**
     * Encodes everything that has been constructed so far in this JSONTree object into a JSON
     * formatted string.
     *
     * @return a string representing data structured in JSON format
     */
    @Override
    public String toString() {
        return tree.toString();
    }
}
