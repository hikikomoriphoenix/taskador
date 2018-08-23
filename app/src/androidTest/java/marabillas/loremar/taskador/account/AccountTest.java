package marabillas.loremar.taskador.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import marabillas.loremar.taskador.App;

import static marabillas.loremar.taskador.utils.AccountUtils.createAccount;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class AccountTest {
    @Test
    public void testGetAuthToken() {
        Context context = App.getInstance().getApplicationContext();
        String name = "test1";
        String type = context.getPackageName();

        AccountManager am = AccountManager.get(context);
        Account account = new Account(name, type);

        // Setting a null authToken to this account, will make getAuthToken use DirectLogin class
        // to login and get authToken from backend server.
        createAccount(context, name, "password", null);

        try {
            String authToken = am.blockingGetAuthToken(account, "full_access", true);
            assertThat(authToken.length(), is(32));
        } catch (OperationCanceledException e) {
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AuthenticatorException e) {
            Assert.fail(e.getMessage());
        }
    }

}
