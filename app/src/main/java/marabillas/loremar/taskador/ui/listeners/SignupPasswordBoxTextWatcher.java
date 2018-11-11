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

import android.text.Editable;
import android.text.TextWatcher;

import marabillas.loremar.taskador.ui.activity.SignupActivity;

/**
 * Listens to text events in signup screen's password box.
 */
public class SignupPasswordBoxTextWatcher implements TextWatcher {
    private SignupActivity signupActivity;

    public SignupPasswordBoxTextWatcher(SignupActivity signupActivity) {
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
        signupActivity.onPasswordBoxTextChanged(String.valueOf(s));
    }
}
