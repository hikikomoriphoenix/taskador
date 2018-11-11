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

package marabillas.loremar.taskador.background;

/**
 * Represents an object that will perform background tasks for a client that requests it.
 *
 * @param <A> A class implementing {@link BackgroundServiceClient} or its extending interface,
 *           for the client object which will request background tasks.
 */
public interface BackgroundTasker<A extends BackgroundServiceClient> {
    /**
     * Bind a {@link BackgroundServiceClient} object to this object. BackgroundTasker will
     * perform background tasks for this client.
     *
     * @param client an instance of
     * {@link BackgroundServiceClient}.
     */
    void bindClient(A client);

    A getClient();
}
