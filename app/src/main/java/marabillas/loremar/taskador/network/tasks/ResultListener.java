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

package marabillas.loremar.taskador.network.tasks;

import marabillas.loremar.taskador.json.JSON;

/**
 * A {@link ResultListener} is notified when a response has been received from the back-server and
 * validated by a {@link marabillas.loremar.taskador.network.BackEndResponseHandler}, by calling
 * one of the callback methods corresponding to the status of the response.
 */
public interface ResultListener {
    /**
     * Callback method for response with 200 OK status.
     */
    void onStatusOK(String message, JSON data);

    /**
     * Callback method for response with 400 or 422 status
     */
    void onClientError(String message);

    /**
     * Callback method for response with 500 INTERNAL SERVER ERROR status
     */
    void onServerError(String message);

    /**
     * Callback method for response with 401 Unauthorized status
     */
    void onUnauthorized();
}
