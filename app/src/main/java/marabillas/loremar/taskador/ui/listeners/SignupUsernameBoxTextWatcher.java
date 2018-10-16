package marabillas.loremar.taskador.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import marabillas.loremar.taskador.ui.activity.SignupActivity;

public class SignupUsernameBoxTextWatcher implements TextWatcher {
    private SignupActivity signupActivity;

    public SignupUsernameBoxTextWatcher(SignupActivity signupActivity) {
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
        signupActivity.onUsernameBoxTextChanged(String.valueOf(s));
    }
}
