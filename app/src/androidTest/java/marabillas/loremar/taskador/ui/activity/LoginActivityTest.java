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

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import marabillas.loremar.taskador.App;
import marabillas.loremar.taskador.background.LoginBackgroundTasker;

import static marabillas.loremar.taskador.utils.LogUtils.log;

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>
            (LoginActivity.class);

    @Test
    public void test() {
        LoginActivity loginActivity = activityTestRule.getActivity();

        class LoginBackgroundTaskerTest implements LoginBackgroundTasker {
            private LoginActivity activity;

            @Override
            public void login(String username, String password) {
                log("Logged in using username=" + username + ",password=" + password);
            }

            @Override
            public void switchToSignupScreen() {

            }

            @Override
            public void bindClient(LoginActivity client) {
                activity = client;
            }

            @Override
            public LoginActivity getClient() {
                return activity;
            }
        }

        loginActivity.setBackgroundTasker(new LoginBackgroundTaskerTest());

        await();
    }

    @Test
    public void testWithLoginManager() {
        App.getInstance().setBackgroundTaskManagerSupport(true);
        activityTestRule.launchActivity(new Intent(activityTestRule.getActivity(), LoginActivity
                .class));

        await();
    }

    private void await() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
