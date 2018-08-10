package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import marabillas.loremar.taskador.network.resulthandlers.TestSignup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BackEndAPICallTaskerTest {
    @Test
    public void signup() {
        final String username = "test2";
        final String password = "passwords";
        final TestSignup testSignup = new TestSignup();
        BackEndAPICallTasker.getInstance().signup(testSignup, username, password);
        new Thread() {
            @Override
            public void run() {
                TestSignup.Result result = testSignup.getResult();
                String message = testSignup.getMessage();
                assertThat(result, is(TestSignup.Result.BACK_END_UNABLE_TO_SAVE_NEW_ACCOUNT));
                assertThat(message, is("Server Error!"));
                testSignup.getCountDownLatch().countDown();
            }
        }.start();
        try {
            testSignup.getCountDownLatch().await();
        } catch (InterruptedException e) {
            Assert.fail("Await interrupted: " + e.getMessage());
        }
    }
}