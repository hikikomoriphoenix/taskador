package marabillas.loremar.taskador.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

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
}
