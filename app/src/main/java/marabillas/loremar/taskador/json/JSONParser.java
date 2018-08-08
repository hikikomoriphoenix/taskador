package marabillas.loremar.taskador.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class is used for parsing JSON documents to extract their data.
 */
public class JSONParser {
    /**
     * downloads the content from a given url representing a JSON document and converts it into a
     * java object of a JSONObject class which provides methods to get all of its data.
     *
     * @param url url string representing a JSON document
     * @return an object containg all the data from the JSON document
     */
    public JSON parse(String url) throws FailedToParseException {
        try {
            // download the JSON content
            InputStream inputStream = new URL(url).openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JSONObject json = new JSONObject(sb.toString()); // does all the parsing
            return new JSON(json);
        } catch (IOException e) {
            throw new FailedToParseException(e);
        } catch (JSONException e) {
            throw new FailedToParseException(e);
        }
    }
}