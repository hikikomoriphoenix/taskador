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

import android.support.test.rule.ActivityTestRule;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.background.SignupBackgroundTasker;

import static marabillas.loremar.taskador.utils.LogUtils.log;

/**
 *
 */
public class SignupActivityTest {
    @Rule
    public ActivityTestRule<SignupActivity> activityTestRule = new ActivityTestRule<>
            (SignupActivity.class);

    @Test
    public void test() {
        SignupActivity signupActivity = activityTestRule.getActivity();

        class SignupBackgroundTaskerTest implements SignupBackgroundTasker {
            private SignupActivity activity;

            @Override
            public void checkUsernameAvailability(String username) {
                if (username.equals("1111")) {
                    activity.onUsernameNotAvailable();
                } else {
                    activity.onUsernameIsAvailable();
                }
            }

            @Override
            public void cancelUsernameAvailabilityCheck() {

            }

            @Override
            public void submitNewAccount(String username, String password) {
                log("New account with username=" + username + ",password=" + password + " is " +
                        "submitted.");
            }

            @Override
            public void bindClient(SignupActivity client) {
                activity = client;
            }

            @Override
            public SignupActivity getClient() {
                return activity;
            }
        }

        signupActivity.setBackgroundTasker(new SignupBackgroundTaskerTest());

        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
