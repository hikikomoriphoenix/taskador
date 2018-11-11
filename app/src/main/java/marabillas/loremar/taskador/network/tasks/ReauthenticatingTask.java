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

package marabillas.loremar.taskador.network.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.account.DirectLogin;

/**
 * This type of {@link RunnableTask} will attempt to reauthenticate upon back-end returning an http 401
 * Unauthorized status code, by directly logging in to server to get a new auth token.
 *
 * @param <RH> a subclass of
 * {@link marabillas.loremar.taskador.network.tasks.RunnableTask.ResultHandler} corresponding to
 *            the specific {@link RunnableTask} subclass
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
