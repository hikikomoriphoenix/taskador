package marabillas.loremar.taskador.ui.listeners;

import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.SignupActivity;

public class SignupOnClickListener implements View.OnClickListener {
    private SignupActivity signupActivity;

    public SignupOnClickListener(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_signup_submit_button) {
            signupActivity.onSubmit();
        }
    }
}
