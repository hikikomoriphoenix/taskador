package marabillas.loremar.taskador.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class wraps an array in a JSON data. Use the getter methods to get the value of each
 * element.
 */
public class JSON_Array {
    private JSONArray array;

    JSON_Array(JSONArray array) {
        this.array = array;
    }

    public JSON_Array getArray(int index) throws FailedToGetFieldException {
        try {
            JSONArray subArray = array.getJSONArray(index);
            return new JSON_Array(subArray);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public boolean getBoolean(int index) throws FailedToGetFieldException {
        try {
            return array.getBoolean(index);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public int getCount() {
        return array.length();
    }

    public double getDouble(int index) throws FailedToGetFieldException {
        try {
            return array.getDouble(index);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public int getInt(int index) throws FailedToGetFieldException {
        try {
            return array.getInt(index);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public long getLong(int index) throws FailedToGetFieldException {
        try {
            return array.getLong(index);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public JSON getObject(int index) throws FailedToGetFieldException {
        try {
            JSONObject object = array.getJSONObject(index);
            return new JSON(object);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public String getString(int index) throws FailedToGetFieldException {
        try {
            String string = array.getString(index);
            if (string.equals("null")) {
                return null;
            } else {
                return string;
            }
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    /**
     * @return the JSON array as a string
     */
    public String toString() {
        return array.toString();
    }
}