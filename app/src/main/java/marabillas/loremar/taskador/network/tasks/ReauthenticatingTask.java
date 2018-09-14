package marabillas.loremar.taskador.network.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.account.DirectLogin;

/**
 * This type of RunnableTask will attempt to reauthenticate upon back-end returning an http 401
 * Unauthorized status code, by directly logging in to server to get a new auth token.
 *
 * @param <RH> a subclass of ResultHandler corresponding to the specific RunnableTask subclass
 */
public abstract class ReauthenticatingTask<RH extends RunnableTask.ResultHandler> extends
        RunnableTask<RH> {
    private String username;

    /**
     * @param username account's username
     */
    public ReauthenticatingTask(String username) {
        this.username = username;
    }

    public void reauthenticate() {
        Context context = App.getInstance().getApplicationContext();
        AccountManager am = AccountManager.get(context);
        Account account = new Account(username, context.getPackageName());
        final String authTokenType = "full_access";

        String authToken = new DirectLogin().loginForToken(am, account, authTokenType);

        onReauthenticationComplete(authToken);
    }

    /**
     * Callback when a new auth token is received. This should be implemented by retrying the
     * task.
     *
     * @param newToken new auth token received from reauthentication.
     */
    public abstract void onReauthenticationComplete(String newToken);
}
