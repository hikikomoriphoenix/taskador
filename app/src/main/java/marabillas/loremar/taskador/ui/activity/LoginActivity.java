package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.R;

/**
 * This activity facilitates the Login screen. The screen is shown to the user when taskador can
 * not authenticate the user. The user have to log in with his/her username and password. The
 * server then returns an auth token which will be used for the user's account authentication.
 * The screen also provides sign up option if he/she doesn't have an account yet.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }
}
