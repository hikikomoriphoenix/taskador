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

/**
 * This exception is thrown when the required field could not be obtained from a parsed data
 */
public class FailedToGetFieldException extends Exception {
    public FailedToGetFieldException() {
    }

    public FailedToGetFieldException(String message) {
        super(message);
    }

    public FailedToGetFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToGetFieldException(Throwable cause) {
        super(cause);
    }
}