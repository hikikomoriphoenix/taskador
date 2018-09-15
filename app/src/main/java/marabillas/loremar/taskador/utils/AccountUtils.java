package marabillas.loremar.taskador.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import junit.framework.Assert;

import java.io.IOException;

import marabillas.loremar.taskador.App;

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
    public static String getAuthToken(String username) {
        Context context = App.getInstance().getApplicationContext();

        AccountManager am = AccountManager.get(context);
        Account account = new Account(username, context.getPackageName());

        String token = null;
        try {
            token = am.blockingGetAuthToken(account, "full_access", true);
        } catch (OperationCanceledException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AuthenticatorException e) {
            Assert.fail(e.getMessage());
        }

        return token;
    }
}
