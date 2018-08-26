package marabillas.loremar.taskador.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.network.resuilthandlers.LoginTest;
import marabillas.loremar.taskador.network.resuilthandlers.SignupTest;
import marabillas.loremar.taskador.network.resuilthandlers.VerifyTokenTest;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BackEndAPICallTaskerTest {
    @Test
    public void signup() {
        final String username = "test3";
        final String password = "password";
        SignupTest signupTest = new SignupTest();

        BackEndAPICallTasker.getInstance().signup(signupTest, username, password);

        signupTest.waitForResults();
    }

    @Test
    public void login() {
        final String username = "test1";
        final String password = "password";
        LoginTest loginTest = new LoginTest();

        BackEndAPICallTasker.getInstance().login(loginTest, username, password);

        loginTest.waitForResults();
    }

    @Test
    public void verifyToken() {
        Context context = App.getInstance().getApplicationContext();

        String username = "test1";
        String type = context.getPackageName();

        AccountManager am = AccountManager.get(context);
        Account account = new Account(username, type);

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

        VerifyTokenTest verifyTokenTest = new VerifyTokenTest();

        BackEndAPICallTasker.getInstance().verifyToken(verifyTokenTest, username, token);

        verifyTokenTest.waitForResults();
    }
}