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

package marabillas.loremar.taskador.utils;

/**
 * Static methods for operation using regular expressions.
 */
public final class RegexUtils {
    private RegexUtils() {
    }

    /**
     * Check if username is 1-16 alphanumeric characters.
     *
     * @param username username to check
     * @return true if it is 1-16 alphanumeric characters. False otherwise.
     */
    public static boolean validateUsername(String username) {
        return username.matches("^[\\p{L}0-9]{1,16}$");
    }

    /**
     * Check if password is 6-16 non-whitespace characters.
     *
     * @param password password to check
     * @return true if it is 6-16 non-whitespace characters. False otherwise.
     */
    public static boolean validatePassword(String password) {
        return password.matches("^[\\S]{6,16}$");
    }
}
