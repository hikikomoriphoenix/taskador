package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.listeners.SignupUsernameBoxTextWatcher;

import static marabillas.loremar.taskador.utils.RegexUtils.validateUsername;

/**
 * This activity displays the signup screen where user can submit and register a new taskador
 * account.
 */
public class SignupActivity extends Activity {
    private TextView usernameInvalid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        usernameInvalid = findViewById(R.id.activity_signup_username_invalid);

        EditText usernameBox = findViewById(R.id.activity_signup_username_box);

        usernameBox.addTextChangedListener(new SignupUsernameBoxTextWatcher(this));
    }

    public void onUsernameBoxTextChanged(String text) {
        boolean valid = validateUsername(text);
        if (!valid) {
            usernameInvalid.setVisibility(View.VISIBLE);
        } else {
            usernameInvalid.setVisibility(View.GONE);
        }
    }
}
