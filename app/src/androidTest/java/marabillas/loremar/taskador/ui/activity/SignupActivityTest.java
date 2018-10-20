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
