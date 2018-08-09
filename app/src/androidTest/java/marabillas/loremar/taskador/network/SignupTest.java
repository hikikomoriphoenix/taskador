package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignupTest {
    @Rule
    public ActivityTestRule<SignupTestActivity> activityTestRule = new ActivityTestRule<>(SignupTestActivity
            .class);

    @Test
    public void testSignup() {
        final SignupTestActivity activity = activityTestRule.getActivity();
        BackEndAPICallTasker.getInstance().setActivity(activity);
        BackEndAPICallTasker.getInstance().signup("test1", "password");
        final Object syncKey = new Object();
        final SignupTestActivity.Result[] result = new SignupTestActivity.Result[1];
        new Thread() {
            @Override
            public void run() {
                try {
                    syncKey.wait();
                    result[0] = activity.getResult();
                    assertThat(result[0], is(equalTo(SignupTestActivity.Result.NEW_ACCOUNT_SAVED)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
