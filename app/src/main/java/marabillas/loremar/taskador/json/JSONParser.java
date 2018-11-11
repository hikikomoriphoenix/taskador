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