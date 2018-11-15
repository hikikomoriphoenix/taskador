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

package marabillas.loremar.taskador.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.background.BackgroundTaskManager;
import marabillas.loremar.taskador.background.BackgroundTasker;
import marabillas.loremar.taskador.background.SignupBackgroundTasker;
import marabillas.loremar.taskador.background.SignupManager;
import marabillas.loremar.taskador.ui.components.SignupInterface;
import marabillas.loremar.taskador.ui.listeners.SignupConfirmPasswordTextWatcher;
import marabillas.loremar.taskador.ui.listeners.SignupOnClickListener;
import marabillas.loremar.taskador.ui.listeners.SignupPasswordBoxTextWatcher;
import marabillas.loremar.taskador.ui.listeners.SignupUsernameBoxTextWatcher;

import static marabillas.loremar.taskador.utils.RegexUtils.validatePassword;
import static marabillas.loremar.taskador.utils.RegexUtils.validateUsername;

/**
 * This activity displays the signup screen where user can submit and register a new taskador
 * account.
 */
public class SignupActivity extends BaseActivity implements SignupInterface {
    private SignupBackgroundTasker signupBackgroundTasker;

    private View usernameInvalid;
    private View usernameAvailability;
    private View passwordInvalid;
    private View confirmPasswordNotMatch;

    private ProgressBar usernameProgress;
    private ImageView usernameIsAvailable;
    private ImageView usernameNotAvailable;
    private TextView usernameAvailabilityTextView;

    private EditText usernameBox;
    private EditText passwordBox;
    private EditText confirmPasswordBox;
    private Button submitButton;

    private CountDownTimerToRequestUsernameAvailabilityCheck timerToRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        usernameInvalid = findViewById(R.id.activity_signup_username_invalid_section);
        usernameAvailability = findViewById(R.id
                .activity_signup_username_availabilitycheck_section);
        usernameProgress = findViewById(R.id.activity_signup_username_availability_progress);
        usernameIsAvailable = findViewById(R.id.activity_signup_username_availability_check);
        usernameNotAvailable = findViewById(R.id.activity_signup_username_alertunavailable);
        usernameAvailabilityTextView = findViewById(R.id
                .activity_signup_username_availability_textview);
        passwordInvalid = findViewById(R.id.activity_signup_password_invalid_section);
        confirmPasswordNotMatch = findViewById(R.id.activity_signup_confirm_password_notmatch_section);

        usernameBox = findViewById(R.id.activity_signup_username_box);
        passwordBox = findViewById(R.id.activity_signup_password_box);
        confirmPasswordBox = findViewById(R.id.activity_signup_confirm_password_box);
        submitButton = findViewById(R.id.activity_signup_submit_button);

        SignupOnClickListener signupOnClickListener = new SignupOnClickListener(this);

        usernameBox.addTextChangedListener(new SignupUsernameBoxTextWatcher(this));
        passwordBox.addTextChangedListener(new SignupPasswordBoxTextWatcher(this));
        confirmPasswordBox.addTextChangedListener(new SignupConfirmPasswordTextWatcher(this));
        submitButton.setOnClickListener(signupOnClickListener);
    }

    @Override
    public void onSetupBackgroundService() {
        setupBackgroundService(SignupManager.class);
    }

    @Override
    public void setupBackgroundService(Class<? extends BackgroundTaskManager> serviceClass) {
        setupBackgroundService(serviceClass, this);
    }

    @Override
    public void setBackgroundTasker(BackgroundTasker backgroundTasker) {
        signupBackgroundTasker = (SignupBackgroundTasker) backgroundTasker;
        signupBackgroundTasker.bindClient(this);
    }

    @Override
    public void onServiceConnected(BackgroundTaskManager backgroundTaskManager) {
        signupBackgroundTasker = (SignupBackgroundTasker) backgroundTaskManager;
        signupBackgroundTasker.bindClient(this);
    }

    public void onUsernameBoxTextChanged(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean valid = validateUsername(text);
                if (!valid) {
                    // Display text stating invalid username.
                    usernameInvalid.setVisibility(View.VISIBLE);
                    usernameAvailability.setVisibility(View.GONE);
                } else {
                    signupBackgroundTasker.cancelUsernameAvailabilityCheck();

                    // Display rotating progress bar and the text "Checking Availability...". Hide
                    // inappropriate views.
                    usernameInvalid.setVisibility(View.GONE);
                    usernameAvailability.setVisibility(View.VISIBLE);
                    usernameIsAvailable.setVisibility(View.GONE);
                    usernameNotAvailable.setVisibility(View.GONE);
                    usernameProgress.setVisibility(View.VISIBLE);
                    int color = ContextCompat.getColor(SignupActivity.this, R.color
                            .activity_signup_username_availability_textcolor);
                    usernameAvailabilityTextView.setTextColor(color);
                    usernameAvailabilityTextView.setText(R.string.activity_signup_username_checking_availability);

                    // Track the time elapsed the user is not typing using a countdown timer resetting
                    // countdown every time the user types. When countdown ends, start checking for
                    // username availability.
                    if (timerToRequest != null) {
                        timerToRequest.cancel();
                        timerToRequest.start();
                    } else {
                        timerToRequest = new CountDownTimerToRequestUsernameAvailabilityCheck(1500, 1500);
                        timerToRequest.start();
                    }
                }
            }
        });

    }

    private class CountDownTimerToRequestUsernameAvailabilityCheck extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        CountDownTimerToRequestUsernameAvailabilityCheck(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            runOnUiThread(new Runnable() {
                public void run() {
                    String username = String.valueOf(usernameBox.getText());
                    signupBackgroundTasker.checkUsernameAvailability(username);
                }
            });
        }
    }

    public void onUsernameIsAvailable() {
        runOnUiThread(new Runnable() {
            public void run() {
                // Display a check mark and the text "Username is available". Hide inappropriate views.
                usernameIsAvailable.setVisibility(View.VISIBLE);
                usernameProgress.setVisibility(View.GONE);
                usernameNotAvailable.setVisibility(View.GONE);
                usernameAvailabilityTextView.setText(R.string.activity_signup_username_isavailable);
            }
        });
    }

    public void onUsernameNotAvailable() {
        runOnUiThread(new Runnable() {
            public void run() {
                // Display warning sign and the text "Username already exists" in red. Hide inappropriate
                // views.
                usernameNotAvailable.setVisibility(View.VISIBLE);
                usernameProgress.setVisibility(View.GONE);
                usernameIsAvailable.setVisibility(View.GONE);
                int color = ContextCompat.getColor(SignupActivity.this, R.color
                        .activity_signup_username_notavailable_textcolor);
                usernameAvailabilityTextView.setTextColor(color);
                usernameAvailabilityTextView.setText(R.string.activity_signup_username_already_exists);
            }
        });
    }

    public void onPasswordBoxTextChanged(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                validatePasswordAndUpdateView(text);
            }
        });
    }

    public void onConfirmPasswordBoxTextChanged(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                confirmPasswordAndUpdateView(text);
            }
        });
    }

    private boolean validateUsernameAndUpdateView(String username) {
        boolean valid = validateUsername(username);
        if (!valid) {
            // Display text stating invalid username.
            usernameInvalid.setVisibility(View.VISIBLE);
            usernameAvailability.setVisibility(View.GONE);
        } else {
            usernameInvalid.setVisibility(View.GONE);
            usernameAvailability.setVisibility(View.GONE);
        }
        return valid;
    }

    private boolean validatePasswordAndUpdateView(String password) {
        boolean valid = validatePassword(password);

        if (valid) {
            passwordInvalid.setVisibility(View.GONE);
        } else {
            // Dispalay text stating invalid password
            passwordInvalid.setVisibility(View.VISIBLE);
        }

        return valid;
    }

    /**
     * Evaluate if text for password confirmation matches with text for desired password. Display
     * text stating otherwise.
     *
     * @param confirmPasswordInput text in 'Confirm Password' EditText for confirming desired
     *                             password.
     * @return true if confirmation password input matches with inputted password. False if
     * otherwise or if unable to confirm.
     */
    private boolean confirmPasswordAndUpdateView(String confirmPasswordInput) {
        String password = String.valueOf(passwordBox.getText());
        if (password != null && confirmPasswordInput != null) {
            boolean match = confirmPasswordInput.equals(password);

            if (match) {
                confirmPasswordNotMatch.setVisibility(View.GONE);
            } else {
                // Display text stating that text inputted for password confirmation does not
                // match text in password.
                confirmPasswordNotMatch.setVisibility(View.VISIBLE);
            }

            return match;
        } else {
            return false;
        }
    }

    /**
     * Method invoked when inputs need to be submitted to register new account. Inputs are first
     * checked if they are valid before submitting.
     */
    public void onSubmit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String username;
                String password;

                username = String.valueOf(usernameBox.getText());
                boolean usernameIsValid;
                if (usernameNotAvailable.getVisibility() == View.VISIBLE) {
                    usernameIsValid = false;
                } else {
                    usernameIsValid = validateUsernameAndUpdateView(username);
                }

                password = String.valueOf(passwordBox.getText());
                boolean passwordIsValid = validatePasswordAndUpdateView(password);

                String confirmPasswordInput = String.valueOf(confirmPasswordBox.getText());
                boolean passwordIsConfirmed = confirmPasswordAndUpdateView(confirmPasswordInput);

                if (usernameIsValid && passwordIsValid && passwordIsConfirmed) {
                    signupBackgroundTasker.submitNewAccount(username, password);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (signupBackgroundTasker instanceof SignupManager) {
            switchScreen(LoginActivity.class, (SignupManager) signupBackgroundTasker, null);
        }
    }

    @Override
    public void switchToAnotherScreen(@NonNull Class<? extends Activity> activityClass, @NonNull BackgroundTaskManager backgroundTaskManager, @Nullable Bundle input) {
        switchScreen(activityClass, backgroundTaskManager, input);
    }
}
