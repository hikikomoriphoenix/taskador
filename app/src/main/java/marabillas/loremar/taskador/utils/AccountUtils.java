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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.ConfigKeys;

import static marabillas.loremar.taskador.utils.LogUtils.logError;

/**
 * Account-related helper methods
 */
public final class AccountUtils {
    private AccountUtils() {
    }

    /**
     * Adds a new account to user's saved accounts. This stores the account type, name, password,
     * and the auth token that will be used to authenticate the account to avoid having to
     * manually log in to the server.
     */
    public static void createAccount(Context context, String name, String password, String
            authToken) {
        Account account = new Account(name, context.getPackageName());

        AccountManager am = AccountManager.get(context);
        am.addAccountExplicitly(account, password, null);
        am.setAuthToken(account, "full_access", authToken);
    }

    /**
     * Get stored auth token for an account with the given username.
     *
     * @param username account username
     * @return auth token as a string
     */
    public static String getAuthToken(String username) throws GetAuthTokenException {
        Context context = App.getInstance().getApplicationContext();

        AccountManager am = AccountManager.get(context);
        Account account = new Account(username, context.getPackageName());

        String token;
        try {
            token = am.blockingGetAuthToken(account, "full_access", true);
        } catch (OperationCanceledException e) {
            logError("OperationCanceledException on getting auth token. Try again.");
            // A second try wouldn't hurt.
            token = getAuthToken(username);
        } catch (IOException e) {
            throw new GetAuthTokenException("IOException on getting auth token: " + e.getMessage());
        } catch (AuthenticatorException e) {
            throw new GetAuthTokenException("AuthenticatorException on getting auth token: " + e
                    .getMessage());
        }

        return token;
    }

    /**
     * Exception on getAuthToken
     */
    public static class GetAuthTokenException extends Exception {
        GetAuthTokenException(String message) {
            super(message);
        }
    }

    /**
     * Get the set current account's username.
     *
     * @return username
     */
    public static String getCurrentAccountUsername() {
        SharedPreferences config = App.getInstance().getSharedPreferences("config", 0);
        return config.getString(ConfigKeys.CURRENT_ACCOUNT_USERNAME, null);
    }

    /**
     * Set the current account by saving its username in SharedPreferences.
     *
     * @param username username of the current account to set.
     */
    public static void setCurrentAccountUsername(String username) {
        SharedPreferences config = App.getInstance().getSharedPreferences("config", 0);
        config.edit().putString(ConfigKeys.CURRENT_ACCOUNT_USERNAME, username).apply();
    }
}
