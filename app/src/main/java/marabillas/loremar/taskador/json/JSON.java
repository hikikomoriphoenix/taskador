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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class wraps the JSON data. Use the getter methods to get each field. An instance of this
 * class is created by parsing through {@link JSONParser}.
 */
public class JSON {
    private JSONObject json;

    // This constructor should be package private to limit its instantiation to JSONParser
    JSON(JSONObject json) {
        this.json = json;
    }

    public JSON_Array getArray(String name) throws FailedToGetFieldException {
        try {
            JSONArray array = json.getJSONArray(name);
            return new JSON_Array(array);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public boolean getBoolean(String name) throws FailedToGetFieldException {
        try {
            return json.getBoolean(name);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public double getDouble(String name) throws FailedToGetFieldException {
        try {
            return json.getDouble(name);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public int getInt(String name) throws FailedToGetFieldException {
        try {
            return json.getInt(name);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public long getLong(String name) throws FailedToGetFieldException {
        try {
            return json.getLong(name);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public JSON getObject(String name) throws FailedToGetFieldException {
        try {
            JSONObject object = json.getJSONObject(name);
            return new JSON(object);
        } catch (JSONException e) {
            throw new FailedToGetFieldException(e);
        } catch (Exception e) {
            throw new FailedToGetFieldException(e);
        }
    }

    public String getString(String name) throws FailedToGetFieldException {
        try {
            String string = json.getString(name);
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
     * @return the whole JSON data as a string
     */
    public String toString() {
        return json.toString();
    }
}