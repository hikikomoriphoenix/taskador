package marabillas.loremar.taskador.ui.activity;

import marabillas.loremar.taskador.network.SignupTask;

/**
 * This is taskador's main launcher activity. It displays a splash screen while taskador is
 * logging in to the server or making transitions from one screen to another.
 */
public class SplashActivity implements SignupTask.Client {
    @Override
    public void newAccountSaved() {

    }

    @Override
    public void failedToSubmitNewAccount() {

    }

    @Override
    public void backEndUnableToSaveNewAccount() {

    }
}
