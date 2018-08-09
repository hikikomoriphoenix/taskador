package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;

import marabillas.loremar.taskador.network.SignupTask;

/**
 * This is taskador's main launcher activity. It displays a splash screen while taskador is
 * logging in to the server or making transitions from one screen to another.
 */
public class SplashActivity extends Activity implements SignupTask.ResultHandler {
    @Override
    public void newAccountSaved(String message) {

    }

    @Override
    public void failedToSubmitNewAccount(String message) {

    }

    @Override
    public void backEndUnableToSaveNewAccount(String message) {

    }
}
