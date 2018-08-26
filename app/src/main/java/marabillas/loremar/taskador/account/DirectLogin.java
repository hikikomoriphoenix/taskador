package marabillas.loremar.taskador.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import java.util.concurrent.CountDownLatch;

import marabillas.loremar.taskador.network.BackEndAPICallTasker;
import marabillas.loremar.taskador.network.tasks.LoginTask;

import static marabillas.loremar.taskador.utils.LogUtils.log;
import static marabillas.loremar.taskador.utils.LogUtils.logError;

/**
 * This class is used for automatically logging in to the back end server.
 */
public class DirectLogin implements LoginTask.ResultHandler {
    private CountDownLatch latch;

    @Override
    public void loggedInSuccessfuly(String message) {
        log("Login success");
        latch.countDown();
    }

    @Override
    public void failedToSubmitLogin(String message) {
        logError(message);
        latch.countDown();
    }

    @Override
    public void loginDenied(String message) {
        logError(message);
        latch.countDown();
    }

    @Override
    public void loginTaskIncomplete(String message) {
        logError(message);
        latch.countDown();
    }

    /**
     * Login to the back-end server. This will use the password stored in the device
     * corresponding to the given account. This is a blocking operation.
     *
     * @return a valid auth token sent by the back-end server upon successful login.
     */
    public String loginForToken(AccountManager accountManager, Account account, String
            authTokenType) {
        String password = accountManager.getPassword(account);
        if (password != null && account.name != null) {
            latch = new CountDownLatch(1);

            // Initiate login and wait for results.
            BackEndAPICallTasker.getInstance().login(this, account.name, password);
            try {
                latch.await();
            } catch (InterruptedException e) {
                logError("Interrupted while logging in: " + e.getMessage());
            }

            return accountManager.peekAuthToken(account, authTokenType);
        } else {
            return null;
        }
    }
}
