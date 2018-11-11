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

package marabillas.loremar.taskador.network;

/**
 * This class tracks the state of cookie-handling when a request receives a cookie from a
 * shared-hosting site instead of a JSON response from taskador's back-end. This ensures that the
 * cookie is handled only once. And if the host still sends a cookie then the response should be
 * declared to be invalid. A cookie is considered handled when the value of cookie is obtained and
 * saved to be later sent along with the same request.
 */
public interface CookieHandledTracker {
    /**
     * Determine if cookie is already handled or not.
     *
     * @return true if cookie is already handled and false if not.
     */
    boolean isCookieHandled();

    /**
     * Code to execute before cookie-handling including setting cookie as not being handled yet.
     */
    void resetCookieHandledTracking();

    /**
     * Code to execute after handling cookie. Set cookie as already handled.
     */
    void finalizeCookieHandling();
}
