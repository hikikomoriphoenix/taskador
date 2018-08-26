package marabillas.loremar.taskador.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used for parsing JSON documents to extract their data.
 */
public class JSONParser {

    /**
     * Convert a string containing JSON data into a java object of JSON class
     *
     * @param jsonString a string containing data in JSON format
     * @return a JSON object
     * @throws FailedToParseException if the given string can not be converted to JSON
     */
    public JSON parse(String jsonString) throws FailedToParseException {
        try {
            JSONObject json = new JSONObject(jsonString); // does all the parsing
            return new JSON(json);
        } catch (JSONException e) {
            throw new FailedToParseException(e);
        }
    }
}