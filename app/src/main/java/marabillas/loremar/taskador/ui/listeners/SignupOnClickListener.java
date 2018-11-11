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

package marabillas.loremar.taskador.ui.listeners;

import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.SignupActivity;

/**
 * Listens to click events in signup screen.
 */
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
