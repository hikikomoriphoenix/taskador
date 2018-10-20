package marabillas.loremar.taskador.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import marabillas.loremar.taskador.ui.activity.SignupActivity;

/**
 * Listen to text events in signup screen's confirm password box.
 */
public class SignupConfirmPasswordTextWatcher implements TextWatcher {
    private SignupActivity signupActivity;

    public SignupConfirmPasswordTextWatcher(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        signupActivity.onConfirmPasswordBoxTextChanged(String.valueOf(s));
    }
}
