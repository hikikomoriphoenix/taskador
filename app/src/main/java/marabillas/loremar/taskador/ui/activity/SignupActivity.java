package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import marabillas.loremar.taskador.R;

/**
 * This activity displays the signup screen where user can submit and register a new taskador
 * account.
 */
public class SignupActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
    }
}
