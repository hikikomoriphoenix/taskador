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

package marabillas.loremar.taskador.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.ui.activity.LoginActivity;

/**
 * This class is used by the {@link AccountManager} for operations related to account
 * authentication. This is bound to the {@link AuthenticatorService}.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private Context context;
    private String accountType;
    private String authTokenType;

    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
        accountType = context.getPackageName();
        authTokenType = "full_access";
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) {
        // Create and return a KEY_INTENT that specifies that the user should be directed to the
        // Login screen when the user chooses to add an account from the device's account settings.

        final Intent intent = new Intent(context, LoginActivity.class);
        App.getInstance().setBackgroundTaskManagerSupport(true);

        intent.putExtra(this.accountType, accountType);
        intent.putExtra(this.authTokenType, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) {
        AccountManager am = AccountManager.get(context);

        String authToken = am.peekAuthToken(account, authTokenType);

        // If no auth token, log in to receive a new auth token.
        if (authToken == null) {
            DirectLogin login = new DirectLogin();
            authToken = login.loginForToken(am, account, authTokenType);
        }

        // If an auth token is retrieved, return the required bundle containing the auth token.
        if (authToken != null) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If an auth token could not be possibly retrieved then the user should be directed to
        // the Login screen for account credentials.
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(this.accountType, account.type);
        intent.putExtra(this.authTokenType, authTokenType);

        Bundle retBundle = new Bundle();
        retBundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return retBundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) {
        return null;
    }
}
